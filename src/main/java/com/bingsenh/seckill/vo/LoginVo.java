package com.bingsenh.seckill.vo;

import lombok.Data;

/**
 * @Author hbs
 * @Date 2019/8/12
 */
@Data
public class LoginVo {
    private String mobile;
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
