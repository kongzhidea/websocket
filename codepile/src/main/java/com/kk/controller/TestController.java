package com.kk.controller;

import com.kk.websocket.persisted.SessionQueue;
import com.kk.websocket.persisted.TSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("test/socket")
public class TestController {

    @Autowired
    SessionQueue sessionQueue;

    @RequestMapping("list")
    @ResponseBody
    public String list(HttpServletRequest request,
                       HttpServletResponse response, Model model) {
        StringBuilder sb = new StringBuilder();
        sb.append("len=" + sessionQueue.getSize() + "\n");

        List<TSession> list = sessionQueue.getAll();
        for (TSession tSession : list) {
            sb.append(tSession.toString() + "\n");
        }
        return sb.toString();
    }

}