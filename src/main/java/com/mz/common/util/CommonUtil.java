package com.mz.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.annotation.FieldMeta;
import com.mz.common.util.reflect.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    /**
     * 枚举值
     *
     * @param propertyValue 参数值如：0
     * @param converterExp  翻译注解的值如：0=男,1=女,2=未知
     * @return 解析后值
     */
    private static String convertByExp(String propertyValue, String converterExp) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, ",")) {
                for (String value : propertyValue.split(",")) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1]).append(",");
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), ",");
    }

    /**
     * 获取对象属性的 字段，旧值, 新值
     *
     * @param oldMap    旧的对象
     * @param newObject 新的对象
     * @return
     */
    public static List<Map<String, Object>> getObjPropOldNew(Map<String, Object> oldMap, Object newObject) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> newMap = BeanUtil.beanToMap(newObject);
            oldMap.forEach((k, v) -> {
                if (newMap.containsKey(k)) {
                    Object newResult = newMap.get(k);
                    newResult = ObjectUtil.isNotEmpty(newResult) ? newResult : "";
                    v = ObjectUtil.isNotEmpty(v) ? v : "";
                    if (!v.equals(newResult)) {
                        Field field = ReflectionUtils.getAccessibleField(newObject, k);
                        if (ObjectUtil.isNotEmpty(field)) {
                            assert field != null;
                            FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
                            if (ObjectUtil.isNotEmpty(fieldMeta) && StringUtils.isNotEmpty(fieldMeta.name())) {
                                Map<String, Object> result = new HashMap<>();
                                result.put("filedName", fieldMeta.name());
                                if (StringUtils.isNotEmpty(fieldMeta.readConverterExp())) {
                                    //翻译表达式 0=男,1=女
                                    String readConverterExp = fieldMeta.readConverterExp();
                                    String oldValue = convertByExp(String.valueOf(v), readConverterExp);
                                    String newValue = convertByExp(String.valueOf(newResult), readConverterExp);
                                    result.put("oldValue", oldValue);
                                    result.put("newValue", newValue);
                                } else {
                                    result.put("oldValue", v);
                                    result.put("newValue", newResult);
                                }
                                list.add(result);
                            }
                        }
                    }
                }
            });
            log.info(String.format("比较的数据:{}%s", list));
            return list;
        } catch (Exception e) {
            log.error("比较异常，原因：", e);
            throw new RuntimeException("比较异常", e);
        }
    }

    public static String getCachePrefix(Map<String, String> headerParamMap) {
        String cacheKeyPrefix = ConstantsCacheUtil.QUERY_SYSCODE_NODECODE;
        if (CollectionUtil.isNotEmpty(headerParamMap)) {
            String sysCode = headerParamMap.getOrDefault("sysCode", "");
            if (StringUtils.isEmpty(sysCode)) {
                sysCode = headerParamMap.getOrDefault("syscode", "");
            }
            if (StringUtils.isNotEmpty(sysCode) && !"undefined".equalsIgnoreCase(sysCode)) {
                cacheKeyPrefix = cacheKeyPrefix.replaceAll("SYSCODE", sysCode);
            }

            String nodeCode = headerParamMap.getOrDefault("nodeCode", "");
            if (StringUtils.isEmpty(nodeCode)) {
                nodeCode = headerParamMap.getOrDefault("nodecode", "");
            }
            if (StringUtils.isNotEmpty(nodeCode) && !"undefined".equalsIgnoreCase(nodeCode)) {
                cacheKeyPrefix = cacheKeyPrefix.replaceAll("NODECODE", nodeCode);
            }
        }
        return cacheKeyPrefix;
    }

    public static AtomicBoolean getCleanFlag(Map<String, Object> oldMap, Object retData) {
        // 是否存在变动
        AtomicBoolean changeFlag = new AtomicBoolean(false);

        // 比较新数据与数据库原数据
        if (ObjectUtil.isNotEmpty(retData) && ObjectUtil.isNotEmpty(oldMap)) {
            List<Map<String, Object>> mapList = CommonUtil.getObjPropOldNew(oldMap, retData);
            for (Map<String, Object> dataMap : mapList) {
                Object oldValue = dataMap.get("oldValue");
                Object newValue = dataMap.get("newValue");
                if (!oldValue.equals(newValue)) {
                    changeFlag.set(true);
                    break;
                }
            }
        }
        return changeFlag;
    }

    /**
     * 判断对象是否为 基本类型或包装类
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitive(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }

        return clazz.equals(void.class) ||
                clazz.equals(Void.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(String.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Date.class);
    }
}
