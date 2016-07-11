package com.kk.websocket.persisted;


import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionQueue {
    private static SessionQueue _instance = new SessionQueue();

    private SessionQueue() {

    }

    public static SessionQueue getInstance() {
        return _instance;
    }

    private Map<Integer, List<TSession>> userSocketSessionMap = new ConcurrentHashMap<Integer, List<TSession>>();

    private Map<String, Integer> sessionId2UserIdMap = new ConcurrentHashMap<String, Integer>();

    public int getSize() {
        return sessionId2UserIdMap.size();
    }

    public List<TSession> getAll() {
        List<TSession> list = new ArrayList<TSession>(userSocketSessionMap.size());
        for (Integer key : userSocketSessionMap.keySet()) {
            list.addAll(userSocketSessionMap.get(key));
        }
        return list;
    }

    public void add(TSession session) {
        List<TSession> list = userSocketSessionMap.get(session.getUserId());
        if (list == null) {
            list = new ArrayList<TSession>();
        }
        list.add(session);
        userSocketSessionMap.put(session.getUserId(), list);

        sessionId2UserIdMap.put(session.getSession().getId(), session.getUserId());
    }

    public void deleteBySessionId(String sessionId) {
        Integer userId = sessionId2UserIdMap.get(sessionId);
        if (userId == null) {
            return;
        }
        deleteByUserIdSessionId(userId, sessionId);
    }

    public void deleteByUserIdSessionId(int userId, String sessionId) {
        List<TSession> list = userSocketSessionMap.get(userId);
        if (list == null) {
            return;
        }
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            WebSocketSession session = list.get(i).getSession();
            if (session.getId().equals(sessionId)) {
                index = i;
            }
        }
        if (index != -1) {
            list.remove(index);
        }
        sessionId2UserIdMap.remove(sessionId);
    }

    public List<TSession> getTSessionListByUserId(int userId) {
        List<TSession> list = userSocketSessionMap.get(userId);
        if (list == null) {
            list = new ArrayList<TSession>();
        }
        return list;
    }

    public TSession getTSessionBySessionId(String sessionId) {
        Integer userId = sessionId2UserIdMap.get(sessionId);
        if (userId == null) {
            return null;
        }

        List<TSession> list = userSocketSessionMap.get(userId);
        if (list == null) {
            return null;
        }

        for (TSession session : list) {
            if (session.getSession().getId().equals(sessionId)) {
                return session;
            }
        }
        return null;
    }
}
