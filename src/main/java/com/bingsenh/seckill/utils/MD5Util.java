package com.bingsenh.seckill.utils;

import org.springframework.util.DigestUtils;

/**
 * Created by bingsenh on 2019/8/11.
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";
    public static String md5(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static String inputPassToFromPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDbPass(String formPass,String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass,String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDbPass(fromPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));
    }



}
