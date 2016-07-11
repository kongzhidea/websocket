package com.byteslounge.servlet;

import com.byteslounge.model.TSession;
import com.byteslounge.service.SessionQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TestServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("act");
        if (action == null) {
            out(response, "action is null");
            return;
        }
        if ("send1".equals(action)) {
            String sessionId = request.getParameter("sessionId");

            TSession session = SessionQueue.getInstance().getTSession(sessionId);

            if (session != null) {
                String msg = request.getParameter("msg");
                session.getSession().getBasicRemote().sendText(msg);
                out(response, msg);
            }
            return;
        }
        if ("send2".equals(action)) {
            int id = Integer.valueOf(request.getParameter("id"));

            List<TSession> list = SessionQueue.getInstance().getTSessionById(id);

            String msg = request.getParameter("msg");
            for (TSession session : list) {
                if (session.getSession().isOpen()) {
                    session.getSession().getBasicRemote().sendText(msg);
                }
            }
            out(response, msg);
            return;
        }
        if ("list.len".equals(action)) {
            out(response, "list.len=" + SessionQueue.getInstance().getList().size());
            for (TSession session : SessionQueue.getInstance().getList()) {
                out(response, "sessionId=" + session.getSession().getId() + ",id=" + session.getId() + ",status=" + session.getSession().isOpen());
            }
            return;
        }

    }

    private void out(HttpServletResponse response, String cont) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(cont);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doGet(req, resp);
    }

}