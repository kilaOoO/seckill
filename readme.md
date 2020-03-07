##  项目背景

本项目是使用 SpringBoot 开发的一个商品限时秒杀系统，除了实现了基本的登录，商品列表查看，秒杀，下单等功能，还针对高并发情况进行了系统缓存，限流的优化。

## 开发工具和环境

工具：Intelij IDEA + Navicat + Git + Jmeter

开发环境：Windows10 +  CentOS 7，2G ，2 processor

## 开发技术

前端：Bootstrap + jQuery + Thymeleaf

后端：SpringBoot  + MyBatis + MySQL + Redis + RabbitMQ

## 实现技术点

### 1. 两次MD5 加密

同过两次 MD5 加密来提高数据的安全性。

- 第一次的作用：防止用户的明文密码在网络上传输  password1 = MD5(明文密码 + 固定 salt)
- 第二次的作用：防止数据库被盗，通过 MD5（彩虹表） 反推出密码。 password2 = MD5(password1 + 随机 salt)

数据库表插入的是第二次加密的密码和随机 salt。

### 2. 分布式 Session

验证用户账户密码都正确情况下，通过 UUID 生成唯一 id 作为 token，再将 token 作为 key，用户信息作为 value 模拟 session 存储到 redis，同时将 token 存储到 cookie，保持登录状态。

```java
// 为用户生成 token,写入 redis,同时存入 cookie 返回给客户端
String token = UUIDUtil.uuid();
addCookie(response,token,user);
private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
    redisService.set(MiaoshaUserKey.token,token,user);
    Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
    cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
    cookie.setPath("/");
    response.addCookie(cookie);
}


// 自定义一个用户参数解析器，每次请求都从 cookie 获取 token 来判断用户状态
public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
    HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
    HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

    String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
    String cookieToken = getCookieValue(request,MiaoshaUserService.COOKIE_NAME_TOKEN);
    if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
        return null;
    }
    String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
    return miaoshaUserService.getByToken(response,token);
}
```

优点：在分布式集群的环境下，服务器间需要同步，定时同步各个服务器的 session 信息，会因为延迟导致 session 不一致，使用 redis 把 session 数据集存储起来，解决 session 不一致问题。

### 3. 商品列表页面缓存

每次请求商品列表页时都需要访问数据库，将对数据库造成较大负担，将商品页的 html 缓存入 redis 并设置一定的过期时间能加速系统页面的访问速度。

```java
public String list(HttpServletRequest request, HttpServletResponse response,Model model, MiaoshaUser user){
    model.addAttribute("user",user);
    // 取缓存
    String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
    if(!StringUtils.isEmpty(html)){
        return html;
    }
    List<GoodsVo> goodsList = goodsService.listGoodsVo();
    model.addAttribute("goodsList",goodsList);
    // 手动渲染
    SpringWebContext ctx = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
    html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
    if(!StringUtils.isEmpty(html)){
        redisService.set(GoodsKey.getGoodsList,"",html);
    }
    return html;
}
```

### 4. 热点数据对象缓存

包括对用户信息，商品信息，订单信息和 token 等经常访问的数据进行对象缓存，减少数据库访问，大大加快查询速度。

```java
// 对于 user 这种热点数据，先取缓存再取数据库而不是每次都去访问数据库
public MiaoshaUser getById(long id){
    // 取缓存
    MiaoshaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
    if(user!=null){
    return user;
    }

    // 取数据库
    user = miaoshaUserDao.getById(id);
    if(user!=null){
    redisService.set(MiaoshaUserKey.getById,""+id,user);
    }
    return  user;
}
```

### 5. 高并发秒杀优化

通过三级缓冲保护，内存标记 、Redis 预减库存 、RabbitMQ 异步下单，这样做的目的是最大力度减少对数据库的访问。

1. 在秒杀阶段使用本地标记对秒杀商品的库存是否充足做标记，若为 true 则直接返回库存不足，为 false 才查询 Redis。通过内存标记减少对 Redis 的访问。
2. 抢购开始前，先将商品库存同步到 redis 中，抢购时先对 Redis 预减库存，若库存不足则直接返回，否则更新数据库。通过 Redis 来减少对数据库访问。
3. 通过 RabbitMQ 将用户请求入队缓冲，实现异步下单。

```java
// 判断是否已经秒杀到了
MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
if(order != null){
    return  Result.error(CodeMsg.REPEATE_MIAOSHA);
}

// 内存标记，减少redis访问
boolean over = localOverMap.get(goodsId);
if(over){
    return Result.error(CodeMsg.MIAO_SHA_OVER);
}

// 预减库存
long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
if(stock<0){
    localOverMap.put(goodsId,true);
    return Result.error(CodeMsg.MIAO_SHA_OVER);
}

// 入队
MiaoshaMessage mm = new MiaoshaMessage();
mm.setUser(user);
mm.setGoodsId(goodsId);
sender.sendMiaoshaMessage(mm);
return Result.success(0);
```

### 6. 解决超卖和重复秒杀

在高并发环境下，对于某一个共享变量的更新，很容易造成线程安全问题，在这里具体表现为超卖现象。

**解决超卖的思路：**

在 SQL 语句中，加入条件判断语句，判断剩余库存是否大于0再去更新。

`update miaosha_goods set stock_count = stock_count-1 where goods_id = #{goodsId} and stock_count > 0`

由于数据库在每次更新的时候会对 miaosha_goods 加锁，因此更新其实是串行执行的，不会出现多个线程同时更新一条记录的情况，所以在这里是通过数据库来保证不会出现超卖现象。

**解决重复秒杀的思路：**

对用户 id 和商品 id 建立一个唯一索引，通过这种约束避免同一用户发同时两个请求秒杀到两件相同商品。

### 7. 数学公式验证码

点击秒杀前，先让用户输入数学公式验证码，验证正确才能进行秒杀。优点如下：

1. 防止恶意机器人刷单
2. 分散用户请求从而缓解服务器压力

