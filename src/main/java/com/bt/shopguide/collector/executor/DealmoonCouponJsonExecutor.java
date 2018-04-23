package com.bt.shopguide.collector.executor;

import com.bt.shopguide.dao.entity.Coupon;
import com.bt.shopguide.dao.service.ICouponService;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by caiting on 2017/11/23.
 */

/** {
 "taskId": 2131482487,
 "seedId": 1397554,
 "createTime": "2017-11-22T15:57:26Z",
 "crawlTime": "2017-11-22T16:00:22Z",
 "url": "http://www.dealmoon.com/Coupons/Samsonite",
 "couponCode": "SAVEMORE",
 "summary": " Spend $100+ and get 25% off; Spend $150+ and get 30% off; Spend $200+ and get 35% off and Free Shipping over $99.",
 "expires": " 2017-11-23 23:59:00",
 "buttonLink": "https://shop.samsonite.com/on/demandware.store/Sites-samsonite-Site/default/Default-Start?siteID=Jv.v1_Wldzg-O6i5vSHM4AfaSj95J6nfYw",
 "couponId": "550383",
 "title": "Samsonite Coupons",
 "listImage": "http://img.dealmoon.com/data/coupon/store-logos/120x60/13518.gif",
 "docId": "1397554.http://www.dealmoon.com/Coupons/Samsonite"
 }
 */
@Service
public class DealmoonCouponJsonExecutor extends AbstractJsonExecutor {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DealmoonCouponJsonExecutor.class);

    @Autowired
    private ICouponService couponService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(String json) {
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();

        String pid = "int_shopguide_goods_dealmoon_coupon_"+obj.get("couponId").getAsString();
        logger.info("coupon cache key:{}",new Object[]{pid});
        if(redisTemplate.opsForValue().get(pid)!=null){
            logger.info("dealmoon coupon-{} has collected!",new Object[]{obj.get("couponId")});
            return;
        }

        Coupon coupon = new Coupon();
        try {
            coupon.setCode((obj.get("couponCode")==null||obj.get("couponCode").equals(JsonNull.INSTANCE))?"":obj.get("couponCode").getAsString());
            coupon.setImgUrl((obj.get("listImage")==null||obj.get("listImage").equals(JsonNull.INSTANCE))?"":obj.get("listImage").getAsString());
            coupon.setSummary((obj.get("summary")==null||obj.get("summary").equals(JsonNull.INSTANCE))?"":obj.get("summary").getAsString());
            coupon.setUrl((obj.get("buttonLink")==null||obj.get("buttonLink").equals(JsonNull.INSTANCE))?"":obj.get("buttonLink").getAsString());
            coupon.setStore((obj.get("title")==null||obj.get("title").equals(JsonNull.INSTANCE))?"":obj.get("title").getAsString().replaceAll(" Coupons",""));
            coupon.setExpire(sdf.parse(obj.get("expires").getAsString()));
            coupon.setCreateTime(new Date());
            if(couponService.save(coupon)>0){
                redisTemplate.opsForValue().set(pid,"1",timeout, TimeUnit.DAYS);
            }else{
                logger.error("save coupon faild!");
            }
        } catch (ParseException e) {
            logger.error("parse expire date faild!");
        } catch (Exception e){
            logger.error("save coupon faild with exception :" + e);
        }
    }
}
