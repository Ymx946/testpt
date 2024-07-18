/*
 * @(#)WebUtil.java 2013-12-2下午06:04:44
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.mz.common.util;

import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Web工具类
 */
public class WebUtil {
    static final Logger log = org.slf4j.LoggerFactory.getLogger(WebUtil.class);
    /**
     * 最大保存url长度
     */
    public static int MAX_URL_SAVE_LENGH = 800;

    public static Map<String, String> getHeads(HttpServletRequest request) {
        Map<String, String> hashMap = new HashMap<>();
        String token = "";
        String loginID = "";
        String loginType = "";
        try {
            token = request.getHeader("token");
            loginID = request.getHeader("loginID");
            loginType = request.getHeader("loginType");
        } catch (Exception e) {
            // TODO: handle exception
        }
        hashMap.put("token", token);
        hashMap.put("loginID", loginID);
        hashMap.put("loginType", loginType);
        return hashMap;
    }

    /**
     * 获得真实IP地址
     *
     * @param request 请求
     * @return 真实IP地址
     * @throws Exception
     * @author sunju
     * @creationDate. 2011-5-9 上午09:14:17
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        if (null == request) {
            log.error("网络异常");
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
//        log.info("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            log.info("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            log.info("getRemoteAddr ip: " + ip);
        }
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) { // IPv4和IPv6的localhost
            // 客户端和服务端是在同一台机器上、获取本机的IP
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.getHostAddress();
        }
        return ip;
    }

    public static boolean internalIp(String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] addr) {
        if (StringUtils.isNull(addr) || addr.length < 2) {
            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
            default:
                return false;
        }
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        return "127.0.0.1";
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "未知";
    }

    /**
     * 获得请求URL
     *
     * @param request 请求
     * @return 请求URL
     * @author sunju
     * @creationDate. 2011-5-10 下午06:39:11
     */
    public static String getRequestURL(HttpServletRequest request) {
        StringBuilder url = new StringBuilder(request.getRequestURI());
        Map<String, String[]> parameterMap = request.getParameterMap();
        String key = null;
        String[] value = null;
        if (parameterMap != null && parameterMap.size() > 0) {
            url.append("?");
            for (Iterator<?> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
                key = (String) it.next();
                value = parameterMap.get(key);
                if (value != null) {
                    for (String val : value) {
                        url.append(key).append("=").append(val).append("&");
                    }
                }
            }
            url.delete(url.length() - 1, url.length());
        }
        return url.toString();
    }

    /**
     * 获得基路径
     *
     * @param request 请求
     * @return 基路径
     * @author sunju
     * @creationDate. 2012-7-5 下午10:08:02
     */
    public static String getBasePath(HttpServletRequest request) {
        int port = request.getServerPort();
        return request.getScheme() + "://" + request.getServerName() + ((port == 80) ? "" : (":" + port)) + request.getContextPath() + "/";
    }

    /**
     * 获取不需要工程名的路径
     *
     * @param request
     * @return
     */
    public static String getBaseNoContextPath(HttpServletRequest request) {
        int port = request.getServerPort();
        return request.getScheme() + "://" + request.getServerName() + ((port == 80) ? "" : (":" + port)) + "/";
    }

    /**
     * 是否为Ajax请求
     *
     * @param request 请求
     * @return 布尔
     * @author sunju
     * @creationDate. 2011-8-24 下午08:46:46
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        /*
         * iframe提交特殊处理，当作ajax请求处理
         */
        String iframe = request.getParameter("iframe");
        boolean isIframe = (StringUtils.isNotEmpty(iframe)) ? Boolean.valueOf(iframe) : false;
        if (isIframe) {
            return true;
        }
        return request.getHeader("x-requested-with") != null;
    }

    /**
     * 向客户端输出
     *
     * @param outObj      输出的Object
     * @param outEncoding 输出编码
     * @throws IOException
     * @author sunju
     * @creationDate. 2010-9-18 上午00:19:16
     * @response 响应对象
     */
    public static void write(HttpServletResponse response, Object outObj, String outEncoding) throws IOException {
        // 设置默认响应类型
        if (response.getContentType() == null) {
            response.setContentType("text/html");
        }
        response.setCharacterEncoding(outEncoding);
        PrintWriter out = null;
        out = response.getWriter();
        out.print(outObj);
        out.flush();
        out.close();
    }
}
