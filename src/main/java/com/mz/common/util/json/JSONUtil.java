package com.mz.common.util.json;

import static cn.hutool.json.JSONUtil.isJsonArray;
import static cn.hutool.json.JSONUtil.isJsonObj;

public class JSONUtil {

    /**
     * 是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     * @since 3.3.0
     */
    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }


//    /**
//     * 判断是否为json
//     * @param json
//     * @return
//     */
//    public static boolean isJsonObj (String json) {
//        if ("{}".equals(json) || (json.length() > 2 && json.charAt(0) == '{' && json.charAt(json.length()-1) == '}')) {
//            try {
//                JSONObject obj = JSON.parseObject(json);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//            return true;
//        }
//        return false;
//    }
}
