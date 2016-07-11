package com.byteslounge.model;

import javax.websocket.Session;

public class TSession {
    private Session session;
    private int id;

    public TSession() {
    }

    public TSession(Session session) {
        this.session = session;
    }

    public TSession(Session session, int id) {
        this.session = session;
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
