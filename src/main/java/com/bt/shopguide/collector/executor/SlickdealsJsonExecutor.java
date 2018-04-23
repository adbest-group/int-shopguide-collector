package com.bt.shopguide.collector.executor;

import com.bt.shopguide.collector.system.GlobalVariable;
import com.bt.shopguide.collector.util.URLUtil;
import com.bt.shopguide.dao.entity.GoodsDetail;
import com.bt.shopguide.dao.entity.GoodsErrors;
import com.bt.shopguide.dao.entity.GoodsList;
import com.bt.shopguide.dao.entity.Malls;
import com.bt.shopguide.dao.service.IGoodsDetailService;
import com.bt.shopguide.dao.service.IGoodsErrorsService;
import com.bt.shopguide.dao.service.IGoodsListService;
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
 * Created by caiting on 2017/9/12.
 *
 */
@Service
public class SlickdealsJsonExecutor extends AbstractJsonExecutor {
    private static Logger logger = LoggerFactory.getLogger(SlickdealsJsonExecutor.class);

    /**样例
     {
     "taskId": 4026082722,
     "seedId": 114194,
     "createTime": "2017-09-27T18:16:02Z",
     "crawlTime": "2017-09-27T18:00:45Z",
     "url": "http://www.dealmoon.com/79-Seagate-Backup-Plus-4TB/650783.html",
     "pid": "650783",
     "listImage": "http://imgcache.dealmoon.com/fsvr.dealmoon.com/dealmoon/1d8/8a8/217/ee5/7cf/565/442/010/9c2/bf9/0f.jpg_300_0_13_1cd0.jpg",
     "title": "Seagate Backup Plus 4TB External Hard Drive",
     "currentPrice": "$79.99",
     "originalPrice": "$119.99",
     "discount": "$79.99 ($119.99, 33% off)",
     "mall": "Amazon.com",
     "content": "Amazon.com offers the Seagate Backup Plus 4TB External Hard Drive for $79.99. Free shipping.   Features: High-speed USB 3.0 and 2.0 connectivity offers plug-and-play functionality on your PC without the need of an external power supply.",
     "buyButton": "Shop Now",
     "buyButtonLink": "https://www.amazon.com/Seagate-External-Desktop-Storage-STFM4000100/dp/B01H9QMMYE?psc=1&SubscriptionId=07PVPGV9KPJSRT8N9GG2&tag=dealmooncnip-20&linkCode=xm2&camp=2025&creative=165953&creativeASIN=B01H9QMMYE",
     "contentImage": "",
     "contentHtml": "<ul> <br/> <li>Amazon.com offers the <a rel=\"nofollow\" href=\"http://www.dealmoon.com/exec/j/?d=650783\" trk=\"Content-650783\" target=\"_blank\"> Seagate Backup Plus 4TB External Hard Drive</a> for <strong>$79.99</strong>.</li> <br/> <li>Free shipping.</li> <br/></ul> <br/><p>&nbsp;</p> <br/><p><strong>Features: </strong>High-speed USB 3.0 and 2.0 connectivity offers plug-and-play functionality on your PC without the need of an external power supply.</p>",
     "titleHtml": "<span class=\"notice_item\">Expired</span> <a target=\"_blank\" href=\"http://www.dealmoon.com/79-Seagate-Backup-Plus-4TB/650783.html\" title=\"\" class=\"notice_ago event_statics_action\" statrk=\"view-650783-deal--\"> <span class=\"notice_item\"> $79.99 (<i class=\"crossed\">$119.99</i>, 33% off) </span> <span>Seagate Backup Plus 4TB External Hard Drive</span> </a>",
     "expired": "Expired",
     "realTitle": "Expired $79.99 ($119.99, 33% off) Seagate Backup Plus 4TB External Hard Drive",
     "docId": "114194.650783"
     }
     **/

    public static String testJson = "{\n" +
            "    \"taskId\": 4026082722,\n" +
            "    \"seedId\": 114194,\n" +
            "    \"createTime\": \"2017-09-27T18:16:02Z\",\n" +
            "    \"crawlTime\": \"2017-09-27T18:00:45Z\",\n" +
            "    \"url\": \"http://www.dealmoon.com/79-Seagate-Backup-Plus-4TB/650783.html\",\n" +
            "    \"pid\": \"650783\",\n" +
            "    \"listImage\": \"http://imgcache.dealmoon.com/fsvr.dealmoon.com/dealmoon/1d8/8a8/217/ee5/7cf/565/442/010/9c2/bf9/0f.jpg_300_0_13_1cd0.jpg\",\n" +
            "    \"title\": \"Seagate Backup Plus 4TB External Hard Drive\",\n" +
            "    \"currentPrice\": \"$79.99\",\n" +
            "    \"originalPrice\": \"$119.99\",\n" +
            "    \"discount\": \"$79.99 ($119.99, 33% off)\",\n" +
            "    \"mall\": \"Amazon.com\",\n" +
            "    \"content\": \"Amazon.com offers the Seagate Backup Plus 4TB External Hard Drive for $79.99. Free shipping.   Features: High-speed USB 3.0 and 2.0 connectivity offers plug-and-play functionality on your PC without the need of an external power supply.\",\n" +
            "    \"buyButton\": \"Shop Now\",\n" +
            "    \"buyButtonLink\": \"https://www.amazon.com/Seagate-External-Desktop-Storage-STFM4000100/dp/B01H9QMMYE?psc=1&SubscriptionId=07PVPGV9KPJSRT8N9GG2&tag=dealmooncnip-20&linkCode=xm2&camp=2025&creative=165953&creativeASIN=B01H9QMMYE\",\n" +
            "    \"contentImage\": \"\",\n" +
            "    \"contentHtml\": \"<ul> <br/> <li>Amazon.com offers the <a rel=\\\"nofollow\\\" href=\\\"http://www.dealmoon.com/exec/j/?d=650783\\\" trk=\\\"Content-650783\\\" target=\\\"_blank\\\"> Seagate Backup Plus 4TB External Hard Drive</a> for <strong>$79.99</strong>.</li> <br/> <li>Free shipping.</li> <br/></ul> <br/><p>&nbsp;</p> <br/><p><strong>Features: </strong>High-speed USB 3.0 and 2.0 connectivity offers plug-and-play functionality on your PC without the need of an external power supply.</p>\",\n" +
            "    \"titleHtml\": \"<span class=\\\"notice_item\\\">Expired</span> <a target=\\\"_blank\\\" href=\\\"http://www.dealmoon.com/79-Seagate-Backup-Plus-4TB/650783.html\\\" title=\\\"\\\" class=\\\"notice_ago event_statics_action\\\" statrk=\\\"view-650783-deal--\\\"> <span class=\\\"notice_item\\\"> $79.99 (<i class=\\\"crossed\\\">$119.99</i>, 33% off) </span> <span>Seagate Backup Plus 4TB External Hard Drive</span> </a>\",\n" +
            "    \"expired\": \"Expired\",\n" +
            "    \"realTitle\": \"Expired $79.99 ($119.99, 33% off) Seagate Backup Plus 4TB External Hard Drive\",\n" +
            "    \"docId\": \"114194.650783\"\n" +
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
            if(obj.get("buyLink")==null || obj.get("buyLink").equals(JsonNull.INSTANCE) || obj.get("buyLink").getAsString().length()<1){
                return;
            }
            //检查缓存中是否标志已入库
            String pid = "int_shopguide_goods_slickdeals_"+obj.get("pid").getAsString();
            logger.info("good cache key:{}",new Object[]{pid});
            if(redisTemplate.opsForValue().get(pid)!=null){
                logger.info("slickdeals good-{} has collected!",new Object[]{obj.get("pid")});
                return;
            }
            glist = new GoodsList();
            gdetail = new GoodsDetail();
//            glist.setDiscounts(obj.get("discount").getAsString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            glist.setTitle(obj.get("title").getAsString());
            glist.setPrice((obj.get("price")==null || obj.get("price").equals(JsonNull.INSTANCE))?"":obj.get("price").getAsString());
//            glist.setOriginalPrice(obj.get("originalPrice").getAsString());
            glist.setGoodSourceName("www.slickdeals.net");
            String short_content = (obj.get("content")==null || obj.get("content").equals(JsonNull.INSTANCE))?"":obj.get("content").getAsString();
            if(short_content.length()>short_content_length){
                short_content = short_content.substring(0,short_content_length);
            }
            glist.setShortContent(short_content);
            //根据url获取对应的商城名称,获取不到为null
            String url = obj.get("buyLink").getAsString();
//            String mallName = obj.get("mall").getAsString();
            String mallName = null;
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
