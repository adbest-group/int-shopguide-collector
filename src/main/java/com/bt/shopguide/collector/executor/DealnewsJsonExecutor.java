package com.bt.shopguide.collector.executor;

import com.bt.shopguide.dao.entity.GoodsDetail;
import com.bt.shopguide.dao.entity.GoodsErrors;
import com.bt.shopguide.dao.entity.GoodsList;
import com.bt.shopguide.dao.entity.Malls;
import com.bt.shopguide.dao.service.IGoodsDetailService;
import com.bt.shopguide.dao.service.IGoodsErrorsService;
import com.bt.shopguide.dao.service.IGoodsListService;
import com.bt.shopguide.collector.system.GlobalVariable;
import com.bt.shopguide.collector.util.URLUtil;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by caiting on 2017/9/27.
 */
@Service
public class DealnewsJsonExecutor extends AbstractJsonExecutor {
    private static Logger logger = LoggerFactory.getLogger(DealnewsJsonExecutor.class);


    /**样例
     {
     "taskId": 4054802470,
     "seedId": 114523,
     "createTime": "2017-09-28T02:04:25Z",
     "crawlTime": "2017-09-28T00:01:29Z",
     "url": "https://www.dealnews.com/Top-Greener-Motion-Sensor-Light-Switch-for-12-free-shipping-w-Prime/2093281.html",
     "pid": "2093281",
     "listImage": "https://cdlnws.a.ssl.fastly.net/image/upload/c_limit,dpr_auto,f_auto,q_auto,w_auto/content/wcortjt56vnzgnk6x59r",
     "title": "Top Greener Motion Sensor Light Switch",
     "titleHtml": "<a class=\"content-wide-heading\" target=\"\" href=\"https://www.dealnews.com/Top-Greener-Motion-Sensor-Light-Switch-for-12-free-shipping-w-Prime/2093281.html\" data-iref=\"fp-content-wide-heading\"><span class=\"newflag time-1506444534\"></span>Top Greener Motion Sensor Light Switch</a>",
     "discount": "$12 free shipping w/ Prime",
     "mall": "Top Greener PIR Motion Sensor Light Switch",
     "content": "Top Greener via Amazon offers its Top Greener PIR Motion Sensor Light Switch for $14.69. Coupon code \"AN2K4PIG\" drops it to $11.75. Plus, Prime members bag free shipping. That's the lowest price we could find by $10 for a similar switch. It features an adjustable delay time and coverage area of 980 sq. ft. Deal ends October 3.",
     "contentHtml": "<div class=\"content-body\"><br/>  Top Greener via Amazon offers its <br/> <a target=\"_blank\" data-iref=\"fp-content-wide-body\" href=\"https://www.dealnews.com/lw/click.html?20,2,2093281\">Top Greener PIR Motion Sensor Light Switch</a> for $14.69. Coupon code \"AN2K4PIG\" drops it to <br/> <b>$11.75</b>. Plus, Prime members bag <br/> <b>free shipping</b>. That's the lowest price we could find by $10 for a similar switch. It features an adjustable delay time and coverage area of 980 sq. ft. Deal ends October 3. <br/></div>",
     "buyButton": "Shop Now",
     "buyButtonLink": "https://www.amazon.com/dp/B015G8VLNA",
     "expired": "",
     "detailsPageImage": "https://cdlnws.a.ssl.fastly.net/image/upload/f_auto,t_large,q_auto/content/wcortjt56vnzgnk6x59r",
     "docId": "114523.2093281"
     }
     **/

    public static String testJson = "{\n" +
            "    \"taskId\": 4054802470,\n" +
            "    \"seedId\": 114523,\n" +
            "    \"createTime\": \"2017-09-28T02:04:25Z\",\n" +
            "    \"crawlTime\": \"2017-09-28T00:01:29Z\",\n" +
            "    \"url\": \"https://www.dealnews.com/Top-Greener-Motion-Sensor-Light-Switch-for-12-free-shipping-w-Prime/2093281.html\",\n" +
            "    \"pid\": \"2093281\",\n" +
            "    \"listImage\": \"https://cdlnws.a.ssl.fastly.net/image/upload/c_limit,dpr_auto,f_auto,q_auto,w_auto/content/wcortjt56vnzgnk6x59r\",\n" +
            "    \"title\": \"Top Greener Motion Sensor Light Switch\",\n" +
            "    \"titleHtml\": \"<a class=\\\"content-wide-heading\\\" target=\\\"\\\" href=\\\"https://www.dealnews.com/Top-Greener-Motion-Sensor-Light-Switch-for-12-free-shipping-w-Prime/2093281.html\\\" data-iref=\\\"fp-content-wide-heading\\\"><span class=\\\"newflag time-1506444534\\\"></span>Top Greener Motion Sensor Light Switch</a>\",\n" +
            "    \"discount\": \"$12 free shipping w/ Prime\",\n" +
            "    \"mall\": \"Top Greener PIR Motion Sensor Light Switch\",\n" +
            "    \"content\": \"Top Greener via Amazon offers its Top Greener PIR Motion Sensor Light Switch for $14.69. Coupon code \\\"AN2K4PIG\\\" drops it to $11.75. Plus, Prime members bag free shipping. That's the lowest price we could find by $10 for a similar switch. It features an adjustable delay time and coverage area of 980 sq. ft. Deal ends October 3.\",\n" +
            "    \"contentHtml\": \"<div class=\\\"content-body\\\"><br/>  Top Greener via Amazon offers its <br/> <a target=\\\"_blank\\\" data-iref=\\\"fp-content-wide-body\\\" href=\\\"https://www.dealnews.com/lw/click.html?20,2,2093281\\\">Top Greener PIR Motion Sensor Light Switch</a> for $14.69. Coupon code \\\"AN2K4PIG\\\" drops it to <br/> <b>$11.75</b>. Plus, Prime members bag <br/> <b>free shipping</b>. That's the lowest price we could find by $10 for a similar switch. It features an adjustable delay time and coverage area of 980 sq. ft. Deal ends October 3. <br/></div>\",\n" +
            "    \"buyButton\": \"Shop Now\",\n" +
            "    \"buyButtonLink\": \"https://www.amazon.com/dp/B015G8VLNA\",\n" +
            "    \"expired\": \"\",\n" +
            "    \"detailsPageImage\": \"https://cdlnws.a.ssl.fastly.net/image/upload/f_auto,t_large,q_auto/content/wcortjt56vnzgnk6x59r\",\n" +
            "    \"docId\": \"114523.2093281\"\n" +
            "}";

    /**
     * 将爬虫抓取的json数据解析成entity
     **/
    public void execute(String json){
        GoodsList glist = null;
        GoodsDetail gdetail = null;
        GoodsErrors gerror = null;
        try {
            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();

            //爬虫可能会被反爬，导致关键字段没内容，这里用buyButtonLink当代表判断是否反爬，如果反爬，直接弃掉
//            这个站点爬的内容大多来自列表页，内容页只有expired，detailsPageImage，通过这俩也不能判断是否反爬
//            if(obj.get("buyButtonLink")==null || obj.get("buyButtonLink").getAsString().length()<1){
//                return;
//            }

            //通过expired字段有内容的话，是过期的，跳过
            if(obj.get("expired")==null || obj.get("expired").equals(JsonNull.INSTANCE) || obj.get("expired").getAsString().length()>0){
                return;
            }
            //检查缓存中是否标志已入库
            String pid = "int_shopguide_goods_dealnews_"+obj.get("pid").getAsString();
            logger.info("good cache key:{}",new Object[]{pid});
            if(redisTemplate.opsForValue().get(pid)!=null){
                logger.info("dealnews good-{} has collected!",new Object[]{obj.get("pid")});
                return;
            }
            glist = new GoodsList();
            gdetail = new GoodsDetail();
            glist.setDiscounts((obj.get("discount")==null || obj.get("discount").equals(JsonNull.INSTANCE))?"":obj.get("discount").getAsString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            glist.setTitle(obj.get("title").getAsString());
//            glist.setPrice(obj.get("currentPrice").getAsString());
//            glist.setOriginalPrice(obj.get("originalPrice").getAsString());
            glist.setGoodSourceName("www.dealnews.com");
            String short_content = (obj.get("content")==null || obj.get("content").equals(JsonNull.INSTANCE))?"":obj.get("content").getAsString();
            if(short_content.length()>short_content_length){
                short_content = short_content.substring(0,short_content_length);
            }
            glist.setShortContent(short_content);
            //根据url获取对应的商城名称,获取不到为null
            String url = obj.get("buyButtonLink").getAsString();
            String mallName = obj.get("mall").getAsString();
            Malls mall = globalVariable.mallMap.get(getMainDomain(url));
            byte publish = 1;
            if(mall !=null){
                mallName = mall.getName();
                glist.setMallName(mallName);
            }else{
//                if(mallName.length()>50){
//                    mallName = mallName.substring(0,50);
//                }
//                publish = 2;
            }
            //商品分类
            String cate = (obj.get("cate")==null || obj.get("cate").equals(JsonNull.INSTANCE))?"":obj.get("cate").getAsString();
            if(StringUtils.isNotEmpty(cate)){
                glist.setCategory(cate);
            }
            //? 这里需转链
            glist.setUrl(dealUrl(url));
            glist.setSmallImageUrl(obj.get("listImage").getAsString());
            glist.setPublish(publish);
            glist.setSyncTime(sdf.parse(obj.get("crawlTime").getAsString().replaceAll("T"," ").replaceAll("Z","")));
            glist.setCreateTime(new Date());
            glist.setThumbs(0);
            //插入goodslist
            int n = 0;
            try {
                n = goodsListService.save(glist);
            }catch(Exception e){
                logger.error("insert into goods_list faild！{}",new Object[]{e.getMessage()});
                gerror = new GoodsErrors();
                gerror.setReason("insert into goods_list faild！"+e.getMessage());
            }
            if(n>0){
                gdetail.setContentHtml((obj.get("contentHtml")==null || obj.get("contentHtml").equals(JsonNull.INSTANCE))?null:obj.get("contentHtml").getAsString().getBytes(charset));
                gdetail.setCreateTime(new Date());
                gdetail.setGoodsId(glist.getId());
                try {
                    goodsDetailService.save(gdetail);
                }catch(Exception e){
                    logger.error("insert into goods_detail faild！{}",new Object[]{e.getMessage()});
                    gerror = new GoodsErrors();
                    gerror.setReason("insert into goods_detail faild！"+e.getMessage());
                }
                redisTemplate.opsForValue().set(pid,"1",timeout, TimeUnit.DAYS);
            }
        }catch (ParseException e) {
            logger.error("parse date error!e:{}",new Object[]{e});
            gerror = new GoodsErrors();
            gerror.setReason("parse date error!e！");

        }catch (Exception e){
            logger.error("parse json error!e:{}",new Object[]{e});
            logger.error("jsonText:"+json);
            gerror = new GoodsErrors();
            gerror.setReason("parse json error！");
        }finally {
            if(gerror!=null){
                try {
                    gerror.setJson(json.getBytes(charset));
                    gerror.setCreateTime(new Date());
                    goodsErrorsService.save(gerror);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    logger.error("json getBytes(\""+charset+"\") error！");
                }
            }
        }
    }
}
