package com.web.mvc.framework.content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebContent {

    private Set<String> excludePathSet;
    private Map<String,String> webPageMap;

    private static WebContent instance = new WebContent();

    private WebContent(){
        webPageMap = new HashMap<>();
        excludePathSet = new HashSet<>();
    }

    public static WebContent getInstance() {
        return instance;
    }

    public Set<String> getExcludePathSet() {
        return excludePathSet;
    }

    public Map<String,String> getWebPageMap() {
        return webPageMap;
    }
}
