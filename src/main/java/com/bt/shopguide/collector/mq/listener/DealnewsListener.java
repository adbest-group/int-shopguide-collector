package com.bt.shopguide.collector.mq.listener;

import com.bt.shopguide.collector.executor.DealmoonJsonExecutor;
import com.bt.shopguide.collector.executor.DealnewsJsonExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by caiting on 2017/9/12.
 */
public class DealnewsListener implements MessageListener {
    Logger logger = LoggerFactory.getLogger(DealnewsListener.class);

    @Autowired
    DealnewsJsonExecutor dealnewsJsonExecutor;

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        String msgJson = null;
        try {
            msgJson = msg.getText();
            dealnewsJsonExecutor.execute(msgJson);
        } catch (JMSException e) {
            logger.error("get MQ message text faild!message:[{}]",new Object[]{msg});
            return;
        }
    }
}
