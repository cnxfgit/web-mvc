package com.web.mvc.util;

import java.lang.reflect.Field;
import java.util.List;

public class $ {

    /**
     * json常量
     */
    private static final char OBJ_LEFT = '{';
    private static final char OBJ_RIGHT = '}';
    private static final char DOUBLE_QUOTED = '\"';
    private static final char COLON = ':';
    private static final String NULL = "null";
    private static final char COMMA = ',';
    private static final char ARR_LEFT = '[';
    private static final char ARR_RIGHT = ']';


    public $(String s){
        toJson(null);
    }

    public static boolean isEmpty(String str){
        return str==null||"".equals(str);
    }

    /**
     * json的arr回调
     */
    public static String toJson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append(ARR_LEFT);
        if (list == null || list.size() == 0) return json.append(ARR_RIGHT).toString();// 空则返回[]
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
        StringBuilder json = new StringBuilder();
        json.append(OBJ_LEFT);
        for (Field field:fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(obj);
                json.append(DOUBLE_QUOTED).append(field.getName()).append(DOUBLE_QUOTED).append(COLON);
                if (fieldValue!=null) {// 不为null的话匹配类型
                    if (fieldValue instanceof String){
                        json.append(DOUBLE_QUOTED).append(fieldValue).append(DOUBLE_QUOTED);
                    }else if (fieldValue instanceof Integer||fieldValue instanceof Long||
                            fieldValue instanceof Byte||fieldValue instanceof Short||
                            fieldValue instanceof Double||fieldValue instanceof Float||
                            fieldValue instanceof Boolean||fieldValue instanceof Character){
                        json.append(fieldValue);
                    }else if (fieldValue instanceof List){
                        List list = (List) fieldValue;
                        json.append(toJson(list));
                    }
                }else {
                    json.append(NULL);
                }
                json.append(COMMA);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return json.substring(0,json.length()-1)+OBJ_RIGHT;// 去掉尾号的逗号加上大括号返回
    }

}
