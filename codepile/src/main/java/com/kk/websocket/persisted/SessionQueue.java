package com.kk.websocket.persisted;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当前为单机模式，后续如果要扩展到集群，再改为mq。
 */
@Service
public class SessionQueue {
    private Log logger = LogFactory.getLog(this.getClass());

    private Map<String, List<TSession>> userSocketSessionMap = new ConcurrentHashMap<>();

    private Map<String, String> sessionId2UserIdMap = new ConcurrentHashMap<>();

    public int getSize() {
        return sessionId2UserIdMap.size();
    }

    public List<TSession> getAll() {
        List<TSession> list = new ArrayList<>(userSocketSessionMap.size());
        for (String key : userSocketSessionMap.keySet()) {
            list.addAll(userSocketSessionMap.get(key));
        }
        return list;
    }

    public void add(TSession session) {
        String userId = session.getUserId();
        synchronized (userId.intern()) {
            List<TSession> list = userSocketSessionMap.get(session.getUserId());
            if (list == null) {
                list = Collections.synchronizedList(new ArrayList<TSession>());
            }
            list.add(session);
            userSocketSessionMap.put(session.getUserId(), list);

            sessionId2UserIdMap.put(session.getSession().getId(), session.getUserId());
        }
    }

    public void deleteBySessionId(String sessionId) {
        String userId = sessionId2UserIdMap.get(sessionId);
        if (userId == null) {
            return;
        }
        deleteByUserIdSessionId(userId, sessionId);
    }

    public void deleteByUserIdSessionId(String userId, String sessionId) {
        synchronized (userId.intern()) {
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
            if (list.size() == 0) {
                userSocketSessionMap.remove(userId);
            }
            sessionId2UserIdMap.remove(sessionId);
        }
    }

    public List<TSession> getTSessionListByUserId(String userId) {
        List<TSession> list = userSocketSessionMap.get(userId);
        if (list == null) {
            list = Collections.synchronizedList(new ArrayList<>());
        }
        return list;
    }

    public int getTSessionCountByUserId(String userId) {
        List<TSession> list = userSocketSessionMap.get(userId);
        if (list == null) {
            list = Collections.synchronizedList(new ArrayList<>());
        }
        return list.size();
    }

    /**
     * 给所有用户Id 发消息.
     *
     * @param userId
     * @param message
     */
    public void sendMessageToUser(String userId, String message) {
        synchronized (userId.intern()) {
            List<TSession> sessionList = getTSessionListByUserId(userId);
            if (sessionList == null || sessionList.size() == 0) {
                return;
            }

            for (TSession item : sessionList) {
                try {
                    item.sendMessage(message);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public TSession getTSessionBySessionId(String sessionId) {
        String userId = sessionId2UserIdMap.get(sessionId);
        if (userId == null) {
            return null;
        }
        synchronized (userId.intern()) {
            List<TSession> list = userSocketSessionMap.get(userId);
            if (list == null) {
                return null;
            }

            for (TSession session : list) {
                if (session.getSession().getId().equals(sessionId)) {
                    return session;
                }
            }
        }
        return null;
    }
}
