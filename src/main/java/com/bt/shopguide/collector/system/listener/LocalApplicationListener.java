package com.bt.shopguide.collector.system.listener;

import com.bt.shopguide.collector.system.GlobalVariable;
import com.bt.shopguide.collector.task.SyncCouponTask;
import com.bt.shopguide.collector.task.SyncGoodsTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by caiting on 2017/9/28.
 */
@Component("LocalApplicationListener")
public class LocalApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GlobalVariable globalVariable;
    @Autowired
    private SyncGoodsTask syncGoodsTask;
    @Autowired
    private SyncCouponTask syncCouponsTask;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent()==null){
            //初始化系统参数和不经常变动的数据库数据
            globalVariable.init();
            //启动商品数据上传
            new Thread(new Runnable(){
                @Override
                public void run() {
                    syncGoodsTask.execute();
                }
            }).start();
            //启动优惠券数据上传
            new Thread(new Runnable(){
                @Override
                public void run() {
                    syncCouponsTask.execute();
                }
            }).start();
        }
    }
}
