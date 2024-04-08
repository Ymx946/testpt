package com.mz.common.util;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

@Slf4j
public class CommonUtil {

    public static void printJson(HttpServletResponse response, int code, String msg, Object data) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(new LinkedHashMap<String, Object>(3) {{
                put("code", code);
                put("msg", msg);
                put("data", data);
            }}));
        } catch (IOException e) {
            log.error("response error ---> {}", e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
