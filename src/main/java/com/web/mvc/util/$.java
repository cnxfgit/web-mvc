package com.web.mvc.util;

import java.lang.reflect.Field;
import java.util.List;

public class $ {

    /**
     * json常量
     */
    private static final String OBJ_LEFT = "{";
    private static final String OBJ_RIGHT = "}";
    private static final String DOUBLE_QUOTED = "\"";
    private static final String COLON = ":";
    private static final String NULL = "null";
    private static final String COMMA = ",";
    private static final String ARR_LEFT = "[";
    private static final String ARR_RIGHT = "]";


    public $(String s){
        toJson(null);
    }

    public boolean isEmpty(String str){
        return str==null||"".equals(str);
    }

    /**
     * json的arr回调
     */
    public static String toJson(List<?> list) {
        if (list == null || list.size() == 0) return ARR_LEFT+ARR_RIGHT;
        StringBuilder json = new StringBuilder(ARR_LEFT);
        for (Object object:list) {
            json.append(toJson(object));
            json.append(COMMA);
        }
        return json.substring(0,json.length()-1)+ARR_RIGHT;
    }

    /**
     *  转化实体类为json对象
     */
    public static String toJson(Object obj){
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder json = new StringBuilder(OBJ_LEFT);
        for (Field field:fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(obj);
                if (fieldValue!=null) {// 不为null的话匹配类型
                    if (fieldValue instanceof String){
                        json.append(DOUBLE_QUOTED).append(field.getName()).append(DOUBLE_QUOTED).append(COLON)
                                .append(DOUBLE_QUOTED).append(fieldValue).append(DOUBLE_QUOTED);
                    }else if (fieldValue instanceof Integer||fieldValue instanceof Long||
                            fieldValue instanceof Byte||fieldValue instanceof Short||
                            fieldValue instanceof Double||fieldValue instanceof Float||
                            fieldValue instanceof Boolean||fieldValue instanceof Character){
                        json.append(DOUBLE_QUOTED).append(field.getName()).append(DOUBLE_QUOTED).append(COLON).append(fieldValue);
                    }else if (fieldValue instanceof List){
                        List list = (List) fieldValue;
                        json.append(DOUBLE_QUOTED).append(field.getName()).append(DOUBLE_QUOTED).append(COLON).append(toJson(list));
                    }
                }else {
                    json.append(DOUBLE_QUOTED).append(field.getName()).append(DOUBLE_QUOTED).append(COLON).append(NULL);
                }
                json.append(COMMA);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return json.substring(0,json.length()-1)+OBJ_RIGHT;// 去掉尾号的逗号加上大括号返回
    }

}
