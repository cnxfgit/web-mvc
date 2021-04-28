package com.web.mvc.framework.util;

import java.lang.reflect.Type;

public class StringUtil {

    public static boolean isEmpty(String str){
        return str==null||"".equals(str);
    }

    public static String getClassName(Type type){
        String typeName = type.getTypeName();
        int start = typeName.indexOf("<") + 1;
        int end = typeName.indexOf(">");
        return typeName.substring(start,end);
    }

}
