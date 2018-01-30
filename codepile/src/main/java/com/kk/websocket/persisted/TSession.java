package com.kk.websocket.persisted;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class TSession {
    private String userId;
    private WebSocketSession session;

    public TSession() {
    }

    public TSession(String userId, WebSocketSession session) {
        this.userId = userId;
        this.session = session;
    }

    public void sendMessage(String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "TSession{" +
                "userId=" + userId +
                ", session.id=" + session.getId() +
                '}';
    }
}
