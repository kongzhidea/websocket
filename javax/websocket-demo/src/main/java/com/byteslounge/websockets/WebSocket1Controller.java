package com.byteslounge.websockets;

import com.byteslounge.model.TSession;
import com.byteslounge.service.SessionQueue;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket1")
public class WebSocket1Controller {

    // 收到客户端消息
    @OnMessage
    public void onMessage(String message, Session session)
            throws IOException, InterruptedException {
        // Print the client message for testing purposes
        System.out.println("Received: " + message);

        // Send the first message to the client
        session.getBasicRemote().sendText("server reveice:" + message);

        // Send 3 messages to the client every 5 seconds
//		int sentMessages = 0;
//		while(sentMessages < 3){
//			Thread.sleep(5000);
//			session.getBasicRemote().
//				sendText("This is an intermediate server message. Count: "
//					+ sentMessages);
//			sentMessages++;
//		}
//
//		// Send a final message to the client
//		session.getBasicRemote().sendText("This is the last server message");
    }

    // 打开链接
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected,sessionId=" + session.getId());
        SessionQueue.getInstance().add(new TSession(session));
    }

    //关闭链接
    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed,sessionId=" + session.getId());
        SessionQueue.getInstance().delete(session.getId());
    }

    /**
     * 异常时触发
     *
     * @param session
     */
    @OnError
    public void onError(Throwable throwable,
                        Session session) {
        throwable.printStackTrace();
        System.out.println("Connection error,sessionId=" + session.getId());
        SessionQueue.getInstance().delete(session.getId());
    }
}
