package com.kk.controller;

import com.kk.websocket.persisted.SessionQueue;
import com.kk.websocket.persisted.TSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("test/socket")
public class TestController {

    @RequestMapping("")
    public String index(HttpServletRequest request,
                        HttpServletResponse response, Model model) {
        return "socket";
    }


    @RequestMapping("list")
    @ResponseBody
    public String list(HttpServletRequest request,
                       HttpServletResponse response, Model model) {
        StringBuilder sb = new StringBuilder();
        sb.append("len=" + SessionQueue.getInstance().getSize() + "\n");

        List<TSession> list = SessionQueue.getInstance().getAll();
        for (TSession tSession : list) {
            sb.append(tSession.toString() + "\n");
        }
        return sb.toString();
    }

    @RequestMapping("sendBySessionId")
    @ResponseBody
    public String sendBySessionId(HttpServletRequest request,
                                  HttpServletResponse response, Model model,
                                  @RequestParam("sessionId") String sessionId,
                                  @RequestParam("msg") String msg
    ) {
        TSession tSession = SessionQueue.getInstance().getTSessionBySessionId(sessionId);
        WebSocketSession session = tSession.getSession();
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(msg));

                return "send.ok";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "send.error";
    }

    @RequestMapping("sendByUserId")
    @ResponseBody
    public String sendByUserId(HttpServletRequest request,
                               HttpServletResponse response, Model model,
                               @RequestParam("userId") int userId,
                               @RequestParam("msg") String msg
    ) {
        List<TSession> list = SessionQueue.getInstance().getTSessionListByUserId(userId);
        for (TSession tSession : list) {
            WebSocketSession session = tSession.getSession();
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "send.ok";
    }
}