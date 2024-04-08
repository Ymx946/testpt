package com.mz.common.util;

import org.springframework.util.StringUtils;

public class RegexUtils {
    public static void main(String[] args) {
        String pwd = "Mz@123456";
        String yh = "f48e10f01dc437e4e00092024f8e0bd1";

        System.out.println(PasswordUtil.md5(yh));

//        System.out.println(RegexUtils.isComplexityMatches3(yh, 8, 20));
    }

    /**
     * 复杂度要求：
     * 大写、小写、数字、特殊字符，需要包含其中至少三项
     *
     * @param content
     * @return
     */
    public static boolean isComplexityMatches(String content) {
        if (!StringUtils.hasLength(content)) {
            return false;
        }

        //1.全部包含：大写、小写、数字、特殊字符；
        String regex1 = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\\W_])^.*$";

        //2.无大写：小写、数字、特殊字符；
        String regex2 = "(?=.*[a-z])(?=.*[0-9])(?=.*[\\W_])^.*$";

        //3.无小写：大写、数字、特殊字符；
        String regex3 = "(?=.*[A-Z])(?=.*[0-9])(?=.*[\\W_])^.*$";

        //4.无数字：大写、小写、特殊字符；
        String regex4 = "(?=.*[A-Z])(?=.*[a-z])(?=.*[\\W_])^.*$";

        //5.无特殊字符：大写、小写、数字；
        String regex5 = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])^.*$";

        String regex = "(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")|(" + regex4 + ")|(" + regex5 + ")";

        return content.matches(regex);
    }

    public static boolean isComplexityMatches2(String content) {
        if (!StringUtils.hasLength(content)) {
            return false;
        }
        //String regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\\W_!@#$%^&*`~()-+=]+$)(?![0-9\\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{8,30}$";
        //String regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,30}$";
        //String regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{5,30}$";
        //String regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{5,}$";
        String regex = "^(?![A-Za-z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[A-Za-z0-9\\W_]{5,}$";//ok
        //String regex = "(?![A-Za-z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)^[A-Za-z0-9\\W_]{5,}$";//ok
        //String regex = "^[A-Za-z0-9\\W_]{5,}$(?![A-Za-z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)";

        //错误的模式，测试结果不正确（此模式匹配的是：大写、小写、数字、特殊字符等四项必须全部包含）
        String regex2 = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{5,30}$";

        return content.matches(regex);
//        return content.matches(regex2);
    }

    public static boolean isComplexityMatches3(String content, Integer minLength, Integer maxLength) {
        if (!StringUtils.hasLength(content)) {
            return false;
        }

        if (minLength != null && maxLength != null && minLength > maxLength) {
            System.out.println("参数非法：最小长度不能大于最大长度。");
            return false;
        }

        if (minLength == null && maxLength != null && maxLength < 0) {
            System.out.println("参数非法：最大长度不能小于0。");
            return false;
        }

        String length = "";
        if (minLength == null || minLength < 1) {
            length = "{0,";
        } else {
            length = "{" + minLength + ",";
        }
        if (maxLength == null) {
            length = length + "}";
        } else {
            length = length + maxLength + "}";
        }

        //String regex = "^(?![A-Za-z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[A-Za-z0-9\\W_]{5,}$";//ok
        String regex = "^(?![A-Za-z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[A-Za-z0-9\\W_]" + length + "$";

        return content.matches(regex);
    }
}
