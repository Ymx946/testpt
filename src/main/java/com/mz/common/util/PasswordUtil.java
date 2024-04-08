package com.mz.common.util;


import com.mz.common.ConstantsUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PasswordUtil {

    //传一个字符串过来经过md5处理返回一个字符串
    //32位小写MD5
    public static String md5(String s) {
        try {
            //MessageDigest是封装md5算法的工具对象还支持SHA算法
            MessageDigest md = MessageDigest.getInstance("MD5");
            //通过digest拿到的任意字符串,得到的bates都是等长的
            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            //这里输出的都是乱码
//	        System.out.println(new String(bytes));
            //返回的toHex通过下面方法再处理
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        //把toHex的字符串把二进制转换成十六进制
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        //循环判断是为了补位操作
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 获取盐值
     */
    public static String salt() {
        //使用UUID通用唯一识别码,取第一个-前面的值
        UUID uuid = UUID.randomUUID();
        String[] arr = uuid.toString().split("-");
        return arr[0];
    }

    /**
     * 设置用户默认密码
     */
    public static Map<String, String> getDefaultPwd(int userLevel) {
        Map<String, String> retMap = new HashMap<>();
        String pwdSalt = salt();
        retMap.put("setPwdSalt", pwdSalt);
        //默认密码  123456   --20220512修改 默认密码
        String oldepwd = md5("123456");
        retMap.put("rawPwdMd5", oldepwd);
        retMap.put("rawPwd", "123456");
        if (userLevel <= 2) {//租户管理员和超管
            oldepwd = md5(ConstantsUtil.PASSWORD_DEFAULT);
            retMap.put("rawPwdMd5", oldepwd);
            retMap.put("rawPwd", ConstantsUtil.PASSWORD_DEFAULT);
        }
        //明码密码前三位+盐+明码密码3位以后---转MD5--最终密码
        String newpwd = md5(oldepwd.substring(0, 3) + pwdSalt + oldepwd.substring(3));
        retMap.put("newpwd", newpwd);
        return retMap;
    }

    /**
     * 根据传入莫玛生成加密密码
     */
    public static Map<String, String> getDefaultPwdByTenant(String passwordDefault) {
        Map<String, String> retMap = new HashMap<>();
        String pwdSalt = salt();
        retMap.put("setPwdSalt", pwdSalt);
        String oldepwd = md5(passwordDefault);
        String newpwd = md5(oldepwd.substring(0, 3) + pwdSalt + oldepwd.substring(3));
        retMap.put("newpwd", newpwd);
        retMap.put("rawPwdMd5", oldepwd);
        retMap.put("rawPwd", passwordDefault);
        return retMap;
    }

    /**
     * 根据传入莫玛生成加密密码
     */
    public static Map<String, String> getDefaultPwd(String passwordMd5) {
        Map<String, String> retMap = new HashMap<>();
        String pwdSalt = salt();
        retMap.put("setPwdSalt", pwdSalt);
        String newpwd = md5(passwordMd5.substring(0, 3) + pwdSalt + passwordMd5.substring(3));
        retMap.put("newpwd", newpwd);
        retMap.put("rawPwdMd5", passwordMd5);
        retMap.put("rawPwd", passwordMd5);
        return retMap;
    }


}
