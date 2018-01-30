package com.kk.websocket.config;

import javax.annotation.Resource;

import com.kk.websocket.interceptor.WebSocketHandShakeInterceptor;
import com.kk.websocket.handler.WebSocketHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Component
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements
        WebSocketConfigurer {

    private static final Log logger = LogFactory.getLog(WebSocketConfig.class);


    @Resource
    private WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws").addInterceptors(
                new WebSocketHandShakeInterceptor()).setAllowedOrigins("*");
        logger.info("成功注册WebSocket服务/ws");
    }

}
