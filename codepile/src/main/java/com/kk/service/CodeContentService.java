package com.kk.service;

import com.kk.consts.Protocol;
import com.kk.dao.CodeContentDao;
import com.kk.model.CodeContent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhihui.kzh
 * @create 30/1/1810:11
 */
@Service
public class CodeContentService {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private CodeContentDao codeContentDao;

    // 只保存最近n份数据，之前数据会清空
    private int LIMIT = 50;

    /**
     * 正文消息
     */
    public void saveContent(String userId, String sessionId, String type, String content) {
        CodeContent codeContent = new CodeContent();
        codeContent.setUserId(userId);
        codeContent.setSessionId(sessionId);

        codeContent.setType(type);

        codeContent.setContent(content);
        codeContent.setGmtCreate(new Date());

        codeContentDao.insert(codeContent);
    }

    // 只保存最近n份数据，之前数据会清空
    public void saveContentLimited(String userId, String sessionId, String type, String content) {
        saveContent(userId, sessionId, type, content);

        int count = getTotalContentByUserId(userId);
        if (count > LIMIT) {
            codeContentDao.deleteContentLimitedByUserId(userId, count - LIMIT);
        }
    }

    // type=content
    public String getLastContent(String userId) {
        return codeContentDao.getLastContent(userId, Protocol.CONTENT.getValue());
    }

    public void deleteContentByUserId(String userId) {
        codeContentDao.deleteContentByUserId(userId);
    }

    public int getTotalContentByUserId(String userId) {
        return codeContentDao.getTotalContentByUserId(userId, Protocol.CONTENT.getValue());
    }

    public void deleteContentBeforeTime(Date time){
        codeContentDao.deleteContentBeforeTime(time);
    }
}
