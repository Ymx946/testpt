package com.mz.common.util;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    public static Object getObject(Object dest, Map<String, Object> addProperties) {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dest);
        Map<String, Class> propertyMap = new HashMap<String, Class>();
        for (PropertyDescriptor d : descriptors) {
            if (!"class".equalsIgnoreCase(d.getName())) {
                propertyMap.put(d.getName(), d.getPropertyType());
            }
        }
        addProperties.forEach((k, v) -> {
            String sclass = v.getClass().toString();
            if (sclass.equals("class java.util.Date")) {//对日期进行处理
                propertyMap.put(k, Long.class);
            } else {
                propertyMap.put(k, v.getClass());
            }

        });
        DynamicBean dynamicBean = new DynamicBean(dest.getClass(), propertyMap);
        propertyMap.forEach((k, v) -> {
            try {
                if (!addProperties.containsKey(k)) {
                    dynamicBean.setValue(k, propertyUtilsBean.getNestedProperty(dest, k));
                }
            } catch (Exception e) {
                logger.error("动态添加字段出错", e);
            }
        });
        addProperties.forEach((k, v) -> {
            try {
                String sclass = v.getClass().toString();
                if (sclass.equals("class java.util.Date")) {//动态添加的字段为date类型需要进行处理
                    Date date = (Date) v;
                    dynamicBean.setValue(k, date.getTime());
                } else {
                    dynamicBean.setValue(k, v);
                }
            } catch (Exception e) {
                logger.error("动态添加字段值出错", e);
            }
        });
        Object obj = dynamicBean.getTarget();
        return obj;
    }

    /**
     * 通过反射判断对象所有字段是否都有值
     *
     * @param obj
     * @return
     */
    public static boolean isFieldNotNull(Object obj) {
        //拿到对象的所有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean flag = false;
        //遍历所有字段
        for (Field field : fields) {
            try {
                //开启权限
                field.setAccessible(true);

                //serialVersionUID是序列化用
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }

                //判断是否有值
                if (!StringUtils.isEmpty(field.get(obj))) {
                    flag = true;
                    return flag;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //关闭权限
                field.setAccessible(false);
            }
        }
        return flag;
    }

//    public static void main(String[] args) {
//        InvestigateInfoRecordMainBodyVO recordMainBodyVO = new InvestigateInfoRecordMainBodyVO();
//        recordMainBodyVO.setMainBodyName("");
//        recordMainBodyVO.setCreatTime("");
//        Map<String, Object> propertiesMap = new HashMap<String, Object>();
//        propertiesMap.put("age", 18);
//        Object obj = getObject(recordMainBodyVO, propertiesMap);
//        System.out.println("动态为User添加age之后，User：" + JSONObject.toJSONString(obj));
//
//        TabZnxpOldinfo tabZnxpOldinfo = new TabZnxpOldinfo();
//        System.out.println(isFieldNotNull(tabZnxpOldinfo));
//
//        tabZnxpOldinfo = new TabZnxpOldinfo();
//        tabZnxpOldinfo.setSex(1);
//        System.out.println(isFieldNotNull(tabZnxpOldinfo));
//    }
}
