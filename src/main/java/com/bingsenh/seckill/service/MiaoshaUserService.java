package com.bingsenh.seckill.service;

import com.alibaba.druid.util.StringUtils;
import com.bingsenh.seckill.dao.MiaoshaUserDao;
import com.bingsenh.seckill.domain.MiaoshaUser;
import com.bingsenh.seckill.exception.GlobalException;
import com.bingsenh.seckill.redis.MiaoshaUserKey;
import com.bingsenh.seckill.redis.RedisService;
import com.bingsenh.seckill.result.CodeMsg;
import com.bingsenh.seckill.utils.MD5Util;
import com.bingsenh.seckill.utils.UUIDUtil;
import com.bingsenh.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author hbs
 * @Date 2019/8/12
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

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

    public boolean updatePassword(String token,long id,String fromPass){
        // 取user
        MiaoshaUser user = getById(id);
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.fromPassToDbPass(fromPass,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);

        // 处理缓存
        redisService.delete(MiaoshaUserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }

    public String login(HttpServletResponse response,LoginVo loginVo){
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        // 判断手机号是否存在
        MiaoshaUser user = miaoshaUserDao.getById(Long.parseLong(mobile));
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.fromPassToDbPass(formPass,saltDB);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        // 生成 cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);

        return token;
    }


    public MiaoshaUser getByToken(HttpServletResponse response,String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);

        // 延长有效期
        if(user != null){
            addCookie(response,token,user);
        }

        return user;
    }

    private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
