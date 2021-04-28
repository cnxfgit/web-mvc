package com.web.mvc.framework.content;

import java.util.ArrayList;
import java.util.List;

public class ClassNamesContent {

    private List<String> list;

    private static ClassNamesContent instance = new ClassNamesContent();

    private ClassNamesContent(){
        list = new ArrayList<>();
    }

    public static ClassNamesContent getInstance() {
        return instance;
    }

    public List<String> getList() {
        return list;
    }
}
