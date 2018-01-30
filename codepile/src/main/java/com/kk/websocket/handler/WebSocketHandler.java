package com.kk.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.kk.consts.Passport;
import com.kk.consts.Protocol;
import com.kk.model.c.TMessage;
import com.kk.service.CodeContentService;
import com.kk.websocket.persisted.SessionQueue;
import com.kk.websocket.persisted.TSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

/**
 * WebSocket处理器
 *
 * @author Administrator
 */
@Component
public class WebSocketHandler implements
        org.springframework.web.socket.WebSocketHandler {

    private Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    SessionQueue sessionQueue;

    @Autowired
    CodeContentService codeContentService;

    /**
     * 关闭连接后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {
        logger.info("Websocket:" + session.getId() + "已经关闭");

        deleteSessionByUserId(session);
    }

    private void deleteSessionByUserId(WebSocketSession session) {
        TSession tSession = sessionQueue.getTSessionBySessionId(session.getId());

        sessionQueue.deleteBySessionId(session.getId());

        if (tSession != null) {
            String userId = tSession.getUserId();

            // 通知下线
            notifyOnline(userId, session.getId());

            int count = sessionQueue.getTSessionCountByUserId(userId);
            if (count == 0) {
                logger.info("用户退出：" + userId);
                // 不立即清除，等到期后再删除。
//                codeContentService.deleteContentByUserId(userId);
            }
        }
    }

    /**
     * 连接建立之后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        String userId = (String) session.getAttributes().get(Passport.userId);

        logger.info("Websocket:" + session.getId() + " 建立连接，userId=" + userId);

        TSession tSession = new TSession(userId, session);

        sessionQueue.add(tSession);

        notifyOnline(userId, session.getId());
    }

    // 通知上线/下线
    private void notifyOnline(String userId, String sessionId) {
        TMessage message = new TMessage();
        message.setUserId(userId);
        message.setType(Protocol.ONLINE.getValue());
        message.setSessionId(sessionId);

        int count = sessionQueue.getTSessionCountByUserId(userId);
        if (count == 0) {
            return;
        }

        message.setContent("" + count);

        sessionQueue.sendMessageToUser(userId, JSON.toJSONString(message));
    }

    /**
     * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
     */
    @Override
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> wmessage) throws Exception {
        if (wmessage.getPayloadLength() == 0) {
            return;
        }
        try {
            String msg = wmessage.getPayload().toString();

            TMessage message = JSON.parseObject(msg, TMessage.class);
            message.setSessionId(session.getId());

            // 打日志使用
            {
                TMessage showMg = (TMessage) BeanUtils.cloneBean(message);
                int ln = StringUtils.length(showMg.getContent());
                if (ln > 30) {
                    showMg.setContent(StringUtils.substring(showMg.getContent(), 0, 13) + ".." + StringUtils.substring(showMg.getContent(), ln - 13, ln));
                }
                logger.info(JSON.toJSONString(showMg));
            }

            // 正文消息
            if (Protocol.CONTENT.getValue().equals(message.getType())) {
                TSession tSession = sessionQueue.getTSessionBySessionId(session.getId());
                if (tSession == null) {
                    return;
                }

                sessionQueue.sendMessageToUser(tSession.getUserId(), JSON.toJSONString(message));

                codeContentService.saveContentLimited(tSession.getUserId(), session.getId(), Protocol.CONTENT.getValue(), message.getContent());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
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

        deleteSessionByUserId(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
