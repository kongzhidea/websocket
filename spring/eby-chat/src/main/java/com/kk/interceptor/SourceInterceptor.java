package com.kk.interceptor;

import com.kk.service.ApplicationContextHelper;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhihui.kong
 */
public class SourceInterceptor extends HandlerInterceptorAdapter {
    public static final Log logger = LogFactory.getLog(SourceInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        return true;
    }
    
}

