package com.kk.websocket.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kk.service.ApplicationContextHelper;
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

//            attributes.put("uid", Integer.valueOf(user.getId()));
            // 拦截器

            return true;
        }
        return false;
    }
}
