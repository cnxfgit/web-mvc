package com.web.mvc.testwork.component;

import com.web.mvc.framework.annotation.component.Component;
import com.web.mvc.framework.log.Log;
import com.web.mvc.framework.log.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AopComponent {

    Log logger = LogFactory.getSimpleLog(AopComponent.class);

    public void after(HttpServletRequest request, HttpServletResponse response,String msg){
        logger.info(msg);
    }

    public void before(HttpServletRequest request, HttpServletResponse response,String msg){
        logger.info(msg);
    }

}
