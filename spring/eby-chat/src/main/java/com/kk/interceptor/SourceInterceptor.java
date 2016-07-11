package com.kk.interceptor;

import com.kk.model.User;
import com.kk.service.ApplicationContextHelper;
import com.kk.service.UserService;
import com.kk.utils.MD5Util;
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

    private static UserService userService;

    /**
     * 后台登陆 cookie
     */
    String LOGIN_USER_NAME_COOKIE = "_l_u";
    String LOGIN_USER_TICKET_COOKIE = "_l_t";
    String LOGIN_USER_TIME_STAMP_COOKIE = "_l_timestamp";


    static {
        userService = ApplicationContextHelper.getBean(UserService.class);
    }


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        try {
            // 判断后台权限
            checkRight(request, response);
        } catch (Exception e) {
            String callBack = request.getRequestURI();
            String params = request.getQueryString();
            if (params != null) {
                callBack = callBack + "?" + params;
                callBack = callBack.replace("&", "%26");
            }
            response.sendRedirect("/console/login?callback=" + callBack);
            return false;
        }

        return true;
    }

    // 判断后台权限
    private void checkRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/console/login")) {
            return;
        }

        String userName = getCookie(request, LOGIN_USER_NAME_COOKIE);
        String timestamp = getCookie(request, LOGIN_USER_TIME_STAMP_COOKIE);
        String ticket = getCookie(request, LOGIN_USER_TICKET_COOKIE);
        // 判断时间戳过期
        long timestampL = NumberUtils.toLong(timestamp);
        long deta = 1000l * 60 * 60 * 24 * 7;// 7天有效期
        if ((System.currentTimeMillis() - timestampL) > deta) {
            throw new Exception("cookie过期");
        }
        User user = userService.getUserByName(userName);
        if (user != null
                && ticket != null
                && ticket.equals(MD5Util.generateLoginTicket(userName,
                user.getPassword(), timestamp))) {
        } else {
            throw new Exception("没有权限");
        }

    }

    public String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0)
            return null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(key))
                return cookies[i].getValue();

        }
        return null;
    }
}
