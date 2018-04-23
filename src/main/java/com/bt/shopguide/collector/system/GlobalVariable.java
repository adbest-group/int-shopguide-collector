package com.bt.shopguide.collector.system;

import com.bt.shopguide.dao.entity.Malls;
import com.bt.shopguide.dao.service.IMallsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caiting on 2017/9/28.
 */
@Service
public class GlobalVariable {
    Logger logger = LoggerFactory.getLogger(GlobalVariable.class);

    @Autowired
    private IMallsService mallsService;

    //商城Map,以domain为键，Malls对象为值
    public static Map<String,Malls> mallMap = new HashMap<>();

    public void init(){
        logger.info("初始化全局变量开始！~~~~~~~~~~~~~~~");
        loadMalls();
        logger.info("初始化全局变量完成！~~~~~~~~~~~~~~~");
    }

    public void loadMalls(){
        List<Malls> malls = mallsService.getAllMalls();
        if(malls.size()>0){
            Map<String,Malls> tmpMalls = new HashMap<>();
            for(Malls mall : malls){
                tmpMalls.put(mall.getDomain(),mall);
            }
            mallMap = tmpMalls;
        }
        logger.info("刷新商城完成！~~~~~~~~~~~~~~~");
    }
}
