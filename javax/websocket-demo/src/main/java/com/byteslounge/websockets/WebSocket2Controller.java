package com.byteslounge.websockets;

import com.byteslounge.model.TSession;
import com.byteslounge.service.SessionQueue;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

//code是我的业务标识参数,websocket是连接的路径，可以自行定义
@ServerEndpoint("/websocket2/{id}")
public class WebSocket2Controller {

    // 收到客户端消息
    @OnMessage
    public void onMessage(String message, Session session,
                          @PathParam("id") int id)
            throws IOException, InterruptedException {
        System.out.println("Received: " + message);
        session.getBasicRemote().sendText("server reveice:" + message);

    }

    // 打开链接
    @OnOpen
    public void onOpen(Session session, @PathParam("id") int id) {
        System.out.println("Client connected,sessionId=" + session.getId());
        SessionQueue.getInstance().add(new TSession(session, id));
    }

    //关闭链接
    @OnClose
    public void onClose(Session session, @PathParam("id") int id) {
        System.out.println("Connection closed,sessionId=" + session.getId() + ",id=" + id);
        SessionQueue.getInstance().delete(session.getId());
    }

    /**
     * 异常时触发
     *
     * @param session
     */
    @OnError
    public void onError(@PathParam("id") int id,
                        Throwable throwable,
                        Session session) {
        throwable.printStackTrace();
        System.out.println("Connection error,sessionId=" + session.getId() + ",id=" + id);
        SessionQueue.getInstance().delete(session.getId());
    }
}
