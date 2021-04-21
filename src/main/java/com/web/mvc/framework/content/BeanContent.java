package com.web.mvc.framework.content;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContent {

    private Map<String, Object> beanMap;

    private static BeanContent instance = new BeanContent();

    private BeanContent(){
        beanMap = new ConcurrentHashMap<>();
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
