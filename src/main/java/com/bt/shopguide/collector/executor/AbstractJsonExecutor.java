package com.bt.shopguide.collector.executor;

import com.bt.shopguide.collector.system.GlobalVariable;
import com.bt.shopguide.collector.util.URLUtil;
import com.bt.shopguide.dao.service.IGoodsDetailService;
import com.bt.shopguide.dao.service.IGoodsErrorsService;
import com.bt.shopguide.dao.service.IGoodsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by caiting on 2017/10/25.
 */
public abstract class AbstractJsonExecutor {
    @Autowired
    protected RedisTemplate<String,String> redisTemplate;
    @Autowired
    protected IGoodsListService goodsListService;
    @Autowired
    protected IGoodsDetailService goodsDetailService;
    @Autowired
    protected IGoodsErrorsService goodsErrorsService;
    @Autowired
    protected GlobalVariable globalVariable;
    @Value("${project.charset}")
    protected String charset;
    @Value("${short_content_length}")
    protected int short_content_length;
    @Value("${redis.key_timeout}")
    protected int timeout;

    @Value("${affiliate.account.amazon}")
    protected String amazon_account;
    @Value("${affiliate.account.ebay}")
    protected String ebay_account;


    public abstract void execute(String json);

    protected String getMainDomain(String url){
        String domain = "";
        domain = URLUtil.getMainDomain(url);
        //如果是乐天特殊处理
        if("linksynergy.com".equalsIgnoreCase(domain)){
            try {
                URL url1 = new URL(url);
                String path = url1.getQuery();
                String realUrl = URLUtil.getParameter(path,"RD_PARM1","utf-8");
                if(realUrl != null){
                    domain = getMainDomain(realUrl);
                }else{
                    realUrl = URLUtil.getParameter(path,"murl","utf-8");
                    if(realUrl != null) {
                        domain = getMainDomain(realUrl);
                    }
                }
            } catch (MalformedURLException e) {

            }
        }
        return domain;
    }

    //转链
    protected String dealUrl(String url){
        String link = null;

        String domain = URLUtil.getDomain(url);
        String mainDomain = URLUtil.getMainDomain(url);
        //如果是美国亚马逊
        if("amazon.com".equalsIgnoreCase(mainDomain)){
            if(url.indexOf("tag=")>-1){
                link = url.replaceAll("tag=[^&^=]*","tag="+amazon_account);
            }else{
                if(url.indexOf("?")>-1){
                    link = url += "&tag="+amazon_account;
                }else{
                    link = url += "?tag="+amazon_account;
                }
            }
        //ebay的链接处理
        }else if("rover.ebay.com".equalsIgnoreCase(domain)){
            link = url.replaceAll("campid=[^&^=]*","campid="+ebay_account);
        }else{
            link = url;
        }

        return link;
    }
}
