package com.kk.websocket.interceptor;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.kk.model.User;
import com.kk.service.ApplicationContextHelper;
import com.kk.service.UserService;
import com.kk.utils.MD5Util;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * WebSocket建立连接（握手）和断开
 *
 * @author Administrator
 */
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {

    private static final Log logger = LogFactory.getLog(WebSocketHandShakeInterceptor.class);

    private static UserService userService;

    /**
     * 后台登陆 cookie
     */
    String LOGIN_USER_NAME_COOKIE = "u";
    String LOGIN_USER_TICKET_COOKIE = "t";
    String LOGIN_USER_TIME_STAMP_COOKIE = "s";


    static {
        userService = ApplicationContextHelper.getBean(UserService.class);
    }

    /**
     * 握手（建立连接）后触发
     *
     * @param request
     * @param response
     * @param webSocketHandler
     * @return
     * @throws Exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler webSocketHandler,
                               Exception exception) {
    }

    /**
     * 握手（建立连接）前触发
     *
     * @param request
     * @param response
     * @param webSocketHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler webSocketHandler,
                                   Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;

            HttpServletRequest req = httpRequest.getServletRequest();


            String userName = getCookie(req, LOGIN_USER_NAME_COOKIE);
            String timestamp = getCookie(req, LOGIN_USER_TIME_STAMP_COOKIE);
            String ticket = getCookie(req, LOGIN_USER_TICKET_COOKIE);
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
                logger.error("没有登陆信息，无法连接session");
                return false;
            }

            if (user != null) {
                logger.info("用户 [" + user.getRealname() + "] 已经建立连接");
                attributes.put("uid", Integer.valueOf(user.getId()));
                return true;
            }

            logger.info(" 拦截到请求session is null");
        }
        return false;
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
