package com.mz.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

class MD5_test {
    public final static String md5(String s) {
        try {

            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            byte[] bytes = mdTemp.digest(s.getBytes(StandardCharsets.UTF_8));
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

    public static String salt() {
        //使用UUID通用唯一识别码,取第一个-前面的值
        UUID uuid = UUID.randomUUID();
        String[] arr = uuid.toString().split("-");
        return arr[0];
    }


    public static void main(String[] args) {
        // MD5_Test aa = new MD5_Test();
        String pwdSalt = "43e42f89";
        String oldepwd = "1234567890";
        String b = MD5_test.md5("b");
//        System.out.print(b);
//        System.out.print(MD5_test.md5(b));

        String newpwd = md5(oldepwd.substring(0, 3) + pwdSalt + oldepwd.substring(3));
        System.out.print(MD5_test.md5(newpwd));
    }
}