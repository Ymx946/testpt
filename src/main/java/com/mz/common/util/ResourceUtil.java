package com.mz.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 项目参数工具类
 */
public class ResourceUtil {

    /**
     * 获得请求路径
     *
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
        return requestPath;
    }

    /**
     * 获取配置文件参数
     *
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final Map<Object, Object> getConfigMap(String path) {
        ResourceBundle bundle = ResourceBundle.getBundle(path);
        Set set = bundle.keySet();
        return ConvertUtils.SetToMap(set);
    }

    public static String getSysPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp = path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
        String separator = System.getProperty("file.separator");
        String resultPath = temp.replaceAll("/", separator + separator).replaceAll("%20", " ");
        return resultPath;
    }

    /**
     * 获取项目根目录
     *
     * @return
     */
    public static String getPorjectPath() {
        String nowpath; // 当前tomcat的bin目录的路径 如
        // D:\java\software\apache-tomcat-6.0.14\bin
        String tempdir;
        nowpath = System.getProperty("user.dir");
        tempdir = nowpath.replace("bin", "webapps"); // 把bin 文件夹变到 webapps文件里面
        tempdir += "\\"; // 拼成D:\java\software\apache-tomcat-6.0.14\webapps\sz_pro
        return tempdir;
    }

    public static String getClassPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp = path.replaceFirst("file:/", "");
        String separator = System.getProperty("file.separator");
        String resultPath = temp.replaceAll("/", separator + separator);
        return resultPath;
    }

    public static String getSystempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getSeparator() {
        return System.getProperty("file.separator");
    }

    public static String getParameter(String field) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameter(field);
    }

    /**
     * 判断是不是提交的url路径
     *
     * @param requestPath 路径名称
     * @return
     */
    public static boolean isSavePath(String requestPath) {
        return StringUtils.isNoneBlank(requestPath)
                && (requestPath.startsWith("add")
                || requestPath.startsWith("edit")
                || requestPath.startsWith("update")
                || requestPath.endsWith("batch")
                || requestPath.contains("save")
                || requestPath.contains("update")
                || requestPath.contains("application")
                || requestPath.contains("insert")
                || requestPath.contains("operation"));
    }

}
