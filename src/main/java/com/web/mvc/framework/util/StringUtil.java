package com.web.mvc.framework.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public static String replaceFirst(String str,String target, String replacement){
        for (int i = 0; i < str.length(); i++) {
            if (i<=str.length()-target.length() && str.substring(i,i+target.length()).equals(target)){
                StringBuilder sb = new StringBuilder();
                sb.append(str.substring(0,i));
                sb.append(replacement);
                sb.append(str.substring(i+target.length()));
                return sb.toString();
            }
        }
        return str;
    }

    public static List<String> getChildren(String parent, String start, String end){
        List<String> list = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < parent.length(); i++) {
            if (i<=parent.length()-start.length() && parent.substring(i,i+start.length()).equals(start)){
                index = i;
            }
            if (i<=parent.length()-end.length() && parent.substring(i,i+end.length()).equals(end)){
                list.add(parent.substring(index,i+1));
            }
        }
        return list;
    }

}
