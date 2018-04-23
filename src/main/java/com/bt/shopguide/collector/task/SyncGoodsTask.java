package com.bt.shopguide.collector.task;

import com.bt.shopguide.collector.util.HttpClientHelper;
import com.bt.shopguide.dao.entity.GoodsListWithHtml;
import com.bt.shopguide.dao.service.IGoodsListService;
import com.bt.shopguide.dao.vo.PageDataVo;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by caiting on 2017/10/12.
 */
@Service
public class SyncGoodsTask {
    Logger logger = Logger.getLogger(SyncGoodsTask.class);
    @Autowired
    private IGoodsListService goodsListService;
    @Value("${sync.pageSize}")
    private int PAGE_SIZE;
    @Value("${sync.waiting}")
    private long TIME_WAITING;
    @Value("${sync.errorWaiting}")
    private long TIME_ERROR_WAITING;
    @Value("${sync.apiUrl}")
    private String API_URL;

    private String last_good_id_cfg_path=System.getProperty("user.dir")+ File.separator+"conf"+File.separator+"sync_good_id.ini";
    private Properties prop;
    private long last_good_id;

    private HttpClientHelper helper;

    public SyncGoodsTask(){
        prop = new Properties();

        try {
            prop.load(new FileInputStream(last_good_id_cfg_path));
            this.last_good_id = Long.parseLong(prop.getProperty("last_id"));
            helper = HttpClientHelper.getInstance();
        } catch (IOException e) {
            logger.error("load sync_good_id.ini faild");
            this.last_good_id = 0;
        }catch(NumberFormatException e){
            logger.error("last_id is NaN in sync_good_id.ini");
            this.last_good_id = 0;
        }
    }

    public void execute(){
        //正确读取配置才开始做事
        if(this.last_good_id>0){
            while(true){
                PageDataVo<GoodsListWithHtml> vo = new PageDataVo<>();
                vo.setPageSize(this.PAGE_SIZE);
                Map<String,Object> map = new HashMap<>();
                map.put("id",this.last_good_id);
                vo.setConditionMap(map);
                goodsListService.selectGoodsListPageWithHtml(vo);
                if(vo.getData()!=null&&vo.getData().size()>0){
                    Gson gson = new Gson();
                    Map<String,String> param = new HashMap<>();
                    param.put("json",gson.toJson(vo.getData()));
                    try {
                        String content = this.helper.doPost(API_URL,param);
                        if(content!=null&& StringUtils.isNotEmpty(content)){
                            this.last_good_id = vo.getData().get(vo.getData().size()-1).getId();
                            this.writeToini();
                            logger.info("this.last_good_id updated:"+this.last_good_id);
                            try {
                                //每页上传后休息100ms
                                Thread.sleep(100);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }else{
                            //api访问存在问题，比如404，500等
                            logger.error("sync goods to aip host faild!");
                            try {
                                Thread.sleep(this.TIME_ERROR_WAITING);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        logger.error("connect to aip host faild!");
                        try {
                            Thread.sleep(this.TIME_ERROR_WAITING);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        continue;
                    }
                }else{
                    //当前没有需要可上传，休息TIME_WAITING
                    try {
                        Thread.sleep(this.TIME_WAITING);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }
    }

    private void writeToini(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.last_good_id_cfg_path);
            this.prop.put("last_id",String.valueOf(this.last_good_id));
            this.prop.store(fos, "modify");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("overrite sync_good_id.ini faild! last_good_id is:"+this.last_good_id);
        }finally{
            if(fos!=null) {
                try {
                    fos.flush();
                } catch (Exception e) {
                }
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }


        }

    }

    public long getlast_good_id() {
        return this.last_good_id;
    }

    public static void main(String[] args) {
        String[] cfgs = new String[]{"classpath:applicationContext.xml","classpath:applicationContext-mybatis.xml","classpath:applicationContext-redis.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(cfgs);

        ((SyncGoodsTask)ctx.getBean("syncGoodsTask")).execute();
    }
}
