package com.kk.job;

import com.kk.service.CodeContentService;
import com.kk.utils.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定期清理多余的对话
 */
@Component
public class AutoCleanContentJob {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    CodeContentService codeContentService;

    @PostConstruct
    public void init() {
        try {
            int m1 = 1000 * 60;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                               @Override
                               public void run() {
                                   logger.info("clean content");
                                   Date time = DateUtil.addHour(-6);
                                   codeContentService.deleteContentBeforeTime(time);
                               }
                           }
                    , m1, m1 * 60);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}