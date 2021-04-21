package com.web.mvc.framework.content;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesContent {

    private Map<String, String> propertiesMap;

    private static PropertiesContent instance = new PropertiesContent();

    private PropertiesContent(){
        propertiesMap = new ConcurrentHashMap<>();
    }

    public static PropertiesContent getInstance() {
        return instance;
    }

    public void setProp(String name,String value) {
        propertiesMap.put(name,value);
    }

    public String getProp(String name){
        if (propertiesMap.containsKey(name))
            return propertiesMap.get(name);
        return null;
    }

}
