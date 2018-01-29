package com.kk.websocket.handler;

import java.io.IOException;
import java.util.List;

import com.kk.websocket.persisted.SessionQueue;
import com.kk.websocket.persisted.TSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket处理器
 *
 * @author Administrator
 */
@Component
public class WebSocketHandler implements
        org.springframework.web.socket.WebSocketHandler {

    private static Log logger = LogFactory.getLog(WebSocketHandler.class);


    /**
     * 关闭连接后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {
        logger.info("Websocket:" + session.getId() + "已经关闭");

        SessionQueue.getInstance().deleteBySessionId(session.getId());
    }

    /**
     * 连接建立之后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        //Integer uid = (Integer) session.getAttributes().get("uid");

        TSession tSession = new TSession(0, session);
        
        SessionQueue.getInstance().add(tSession);
    }

    /**
     * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
     */
    @Override
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() == 0) {
            return;
        }
        logger.info(message.getPayload().toString());

        session.sendMessage(new TextMessage("hello，我在处理呢！"));
    }

    /**
     * 消息传输错误处理
     */
    @Override
    public void handleTransportError(WebSocketSession session,
                                     Throwable throwable) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.error("socket.error:" + session.getId());

        SessionQueue.getInstance().deleteBySessionId(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给某个用户发送消息
     *
     * @param uid
     * @param message
     * @throws IOException
     */
    public void sendMessageToUser(Integer uid, TextMessage message)
            throws IOException {
        List<TSession> list = SessionQueue.getInstance().getTSessionListByUserId(uid);
        for (TSession tSession : list) {
            WebSocketSession session = tSession.getSession();
            if (session != null && session.isOpen()) {
                session.sendMessage(message);
            }
        }
    }
}
