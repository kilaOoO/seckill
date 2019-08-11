package com.bingsenh.seckill.result;

import lombok.Data;
import lombok.Getter;

/**
 * Created by bingsenh on 2019/8/10.
 */
@Getter
public class CodeMsg {
    private int code;
    private String msg;

    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");

    private CodeMsg(int code,String msg){
        this.msg = msg;
        this.code = code;
    }
}
