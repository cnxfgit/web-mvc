package com.web.mvc.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanContent {

    private Map<String, Object> beanMap;

    private static BeanContent instance = new BeanContent();

    private BeanContent(){
        beanMap = new HashMap<>();
    }

    public static BeanContent getInstance() {
        return instance;
    }

    public Set<Map.Entry<String, Object>> getEntrySet(){
        return beanMap.entrySet();
    }

    public void setBean(String type,Object object) {
        beanMap.put(type,object);
    }

    public Object getBean(String type){
        if (beanMap.containsKey(type))
            return beanMap.get(type);
        return null;
    }
}
