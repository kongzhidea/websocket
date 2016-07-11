package com.byteslounge.service;

import com.byteslounge.model.TSession;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SessionQueue {
    private static SessionQueue _instance = new SessionQueue();

    private SessionQueue() {

    }

    public static SessionQueue getInstance() {
        return _instance;
    }

    private List<TSession> list = new ArrayList<TSession>();

    public List<TSession> getList() {
        return list;
    }

    public synchronized void add(TSession session) {
        this.list.add(session);
    }

    public synchronized void delete(String sessionId) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            Session session = list.get(i).getSession();
            if (session.getId().equals(sessionId)) {
                index = i;
            }
        }
        if (index != -1) {
            list.remove(index);
        }
    }

    public synchronized TSession getTSession(String sessionId) {
        for (TSession session : list) {
            if (session.getSession().getId().equals(sessionId)) {
                return session;
            }
        }
        return null;
    }

    public synchronized List<TSession> getTSessionById(int id) {
        List<TSession> l = new ArrayList<TSession>();
        for (TSession session : list) {
            if (session.getId() == id) {
                l.add(session);
            }
        }
        return l;
    }
}
