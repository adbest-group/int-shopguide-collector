package com.bt.shopguide.collector.task;

import com.bt.shopguide.collector.system.GlobalVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by caiting on 2017/10/12.
 */
@Component
public class RefreshCache {

    @Autowired
    private GlobalVariable globalVariable;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void refresh(){
        globalVariable.loadMalls();
    }
}
