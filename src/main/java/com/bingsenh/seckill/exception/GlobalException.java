package com.bingsenh.seckill.exception;

import com.bingsenh.seckill.result.CodeMsg;
import lombok.Getter;

/**
 * @Author hbs
 * @Date 2019/8/13
 */
@Getter
public class GlobalException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private CodeMsg cm;
    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }
}
