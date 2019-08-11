package com.bingsenh.seckill.result;

import lombok.Data;
import lombok.Getter;

/**
 * Created by bingsenh on 2019/8/9.
 */
@Getter
public class Result<T> {
    private int code;
    private String msg;
    private T data;


    private Result(T data){
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cm){
        if(cm == null)
            return;
        this.code =cm.getCode();
        this.msg = cm.getMsg();

    }

    //成功
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    //失败
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }
}
