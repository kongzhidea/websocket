package com.kk.websocket.persisted;

import org.springframework.web.socket.WebSocketSession;

public class TSession {
    private int userId;
    WebSocketSession session;

    public TSession() {
    }

    public TSession(int userId, WebSocketSession session) {
        this.userId = userId;
        this.session = session;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
                ", name='" + name + '\'' +
                '}';
    }
}
