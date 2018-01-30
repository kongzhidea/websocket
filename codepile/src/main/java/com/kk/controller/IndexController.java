package com.kk.controller;

import com.kk.service.CodeContentService;
import com.kk.utils.URLUtil;
import com.kk.websocket.persisted.SessionQueue;
import com.kk.websocket.persisted.TSession;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class IndexController {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    SessionQueue sessionQueue;

    @Autowired
    CodeContentService codeContentService;

    @RequestMapping("")
    public String index() {
        return create();
    }

    @RequestMapping("create")
    public String create() {

        String userId = RandomStringUtils.randomAlphanumeric(6);
        int c = 0;
        while (true) {
            logger.info("create " + userId + ",c=" + c);

            int count = sessionQueue.getTSessionCountByUserId(userId);

            int total = codeContentService.getTotalContentByUserId(userId);

            if (count > 0 || total > 0) {
                userId = RandomStringUtils.randomAlphanumeric(6);
                continue;
            }
            break;
        }

        return "redirect:/show/" + userId;
    }

    @RequestMapping("show/{userId}")
    public String show(HttpServletRequest request,
                       HttpServletResponse response, Model model,
                       @PathVariable("userId") String userId) {
        model.addAttribute("userId", userId);

        String domain = URLUtil.getCurrentDomain(request);
        model.addAttribute("domain", domain);

        String content = codeContentService.getLastContent(userId);
        model.addAttribute("content", content);
        return "show";
    }
}