package com.bt.shopguide.collector.executor;

import com.bt.shopguide.dao.entity.GoodsDetail;
import com.bt.shopguide.dao.entity.GoodsErrors;
import com.bt.shopguide.dao.entity.GoodsList;
import com.bt.shopguide.dao.entity.Malls;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DealmoonGiftJsonExecutor extends AbstractJsonExecutor {
    private static Logger logger = LoggerFactory.getLogger(DealmoonGiftJsonExecutor.class);

    /**样例
     {
     "taskId": 2045401,
     "seedId": 1398035,
     "createTime": "2017-12-13T10:55:52Z",
     "crawlTime": "2017-12-13T10:58:21Z",
     "pid": "Gift-Options/736593",
     "Store": "Amazon.com",
     "content": "Amazon.com offers gift options Holiday Snacks Gift Box.",
     "contentImage": "http://imgcache.dealmoon.com/thumbimg.dealmoon.com/dealmoon/957/164/9b9/1ff3a88ea7a933ed8de121a.jpg_300_0_13_e8df.jpg",
     "cate": "All DealsFoodFood & DrinkGift OptionsHoliday Snacks Gift Box @Amazon.com",
     "contentHtml": "<ul><br/> <li>Amazon.com offers <a rel=\"nofollow\" href=\"http://www.dealmoon.com/exec/j/?d=736593\" trk=\"Content-736593\" target=\"_blank\"><strong>gift options</strong> Holiday Snacks Gift Box</a>.</li><br/></ul>",
     "buyButton": "Shop Now",
     "buyButtonLink": "https://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Dgrocery&field-keywords=gift&rh=n%3A16310101%2Ck%3Agift&tag=dealmooncnip-20",
     "pubtime": "Posted 5 days ago",
     "listImage": "http://imgcache.dealmoon.com/thumbimg.dealmoon.com/dealmoon/957/164/9b9/1ff3a88ea7a933ed8de121a.jpg_350_350_2_e910.jpg",
     "discount": "Gift Options",
     "title": "Holiday Snacks Gift Box @Amazon.com",
     "url": "http://www.dealmoon.com/Gift-Options/736593.html",
     "docId": "1398035.Gift-Options/736593"
     }
     **/

    public static String testJson = "{\n" +
            "    \"taskId\": 2045401,\n" +
            "    \"seedId\": 1398035,\n" +
            "    \"createTime\": \"2017-12-13T10:55:52Z\",\n" +
            "    \"crawlTime\": \"2017-12-13T10:58:21Z\",\n" +
            "    \"pid\": \"Gift-Options/736593\",\n" +
            "    \"Store\": \"Amazon.com\",\n" +
            "    \"content\": \"Amazon.com offers gift options Holiday Snacks Gift Box.\",\n" +
            "    \"contentImage\": \"http://imgcache.dealmoon.com/thumbimg.dealmoon.com/dealmoon/957/164/9b9/1ff3a88ea7a933ed8de121a.jpg_300_0_13_e8df.jpg\",\n" +
            "    \"cate\": \"All DealsFoodFood & DrinkGift OptionsHoliday Snacks Gift Box @Amazon.com\",\n" +
            "    \"contentHtml\": \"<ul><br/> <li>Amazon.com offers <a rel=\\\"nofollow\\\" href=\\\"http://www.dealmoon.com/exec/j/?d=736593\\\" trk=\\\"Content-736593\\\" target=\\\"_blank\\\"><strong>gift options</strong> Holiday Snacks Gift Box</a>.</li><br/></ul>\",\n" +
            "    \"buyButton\": \"Shop Now\",\n" +
            "    \"buyButtonLink\": \"https://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Dgrocery&field-keywords=gift&rh=n%3A16310101%2Ck%3Agift&tag=dealmooncnip-20\",\n" +
            "    \"pubtime\": \"Posted 5 days ago\",\n" +
            "    \"listImage\": \"http://imgcache.dealmoon.com/thumbimg.dealmoon.com/dealmoon/957/164/9b9/1ff3a88ea7a933ed8de121a.jpg_350_350_2_e910.jpg\",\n" +
            "    \"discount\": \"Gift Options\",\n" +
            "    \"title\": \"Holiday Snacks Gift Box @Amazon.com\",\n" +
            "    \"url\": \"http://www.dealmoon.com/Gift-Options/736593.html\",\n" +
            "    \"docId\": \"1398035.Gift-Options/736593\"\n" +
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
            //爬虫会把过期标志也当成折扣信息 一部分，这里要处理，过期直接弃掉
            if(obj.get("discount")!=null&&!obj.get("discount").equals(JsonNull.INSTANCE)&&obj.get("discount").getAsString().toLowerCase().contains("overdue") || obj.get("expired") !=null && !obj.get("expired").equals(JsonNull.INSTANCE) && obj.get("expired").getAsString().toLowerCase().contains("expired")){
//                merchandise.setDiscount(obj.get("discount").getAsJsonArray().get(1).getAsString());
                return;
            }
            //爬虫可能会被反爬，导致关键字段没内容，这里用buyButtonLink当代表判断是否反爬，如果反爬，直接弃掉
            if(obj.get("buyButtonLink")==null || obj.get("buyButtonLink").equals(JsonNull.INSTANCE) || obj.get("buyButtonLink").getAsString().length()<1){
                return;
            }
            //检查缓存中是否标志已入库
            String pid = "int_shopguide_gift_dealmoon_"+obj.get("pid").getAsString();
            logger.info("good cache key:{}",new Object[]{pid});
            if(redisTemplate.opsForValue().get(pid)!=null){
                logger.info("dealmoon gift-{} has collected!",new Object[]{obj.get("pid")});
                return;
            }
            glist = new GoodsList();
            gdetail = new GoodsDetail();
            glist.setDiscounts((obj.get("discount")==null || obj.get("discount").equals(JsonNull.INSTANCE))?"":obj.get("discount").getAsString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            glist.setTitle(obj.get("title").getAsString());
//            glist.setPrice((obj.get("currentPrice")==null || obj.get("currentPrice").equals(JsonNull.INSTANCE))?"":obj.get("currentPrice").getAsString());
//            glist.setOriginalPrice((obj.get("originalPrice")==null || obj.get("originalPrice").equals(JsonNull.INSTANCE))?"":obj.get("originalPrice").getAsString());
            glist.setGoodSourceName("www.dealmoon.com");
            String short_content = (obj.get("content")==null || obj.get("content").equals(JsonNull.INSTANCE))?"":obj.get("content").getAsString();
            if(short_content.length()>short_content_length){
                short_content = short_content.substring(0,short_content_length);
            }
            glist.setShortContent(short_content);
            //根据url获取对应的商城名称,获取不到为null
            String url = obj.get("buyButtonLink").getAsString();
            String mallName = obj.get("Store").getAsString();
            String mainDomain = getMainDomain(url);
            Malls mall = globalVariable.mallMap.get(mainDomain);
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

    public static void main(String[] args) {
//        String s = "{\"taskId\":3544263420,\"seedId\":114179,\"createTime\":\"2017-10-25T15:41:38Z\",\"crawlTime\":\"2017-10-25T15:35:58Z\",\"url\":\"http://www.dealmoon.com/99-96-Sunny/688785.html\",\"pid\":\"688785\",\"listImage\":\"http://imgcache.dealmoon.com/fsvrugc.dealmoon.com/ugc/0a5/7b5/de9/f2f/e32/fe8/16d/da8/f5c/288/ea.jpg_300_0_13_f087.jpg\",\"title\":\"Sunny Health & Fitness SF-B1203 Chain Drive Indoor Cycling Trainer Exercise Bike\",\"currentPrice\":null,\"originalPrice\":null,\"discount\":\"$99.96\",\"mall\":\"Walmart\",\"content\":\"Walmart offers the Sunny Health & Fitness SF-B1203 Chain Drive Indoor Cycling Trainer Exercise Bike for $99.96. Amazon has same price. Free shipping.   Features: Indoor cycling bike with 40-pound flywheel, Heavy-duty crank and smooth chain drive mechanism, Fully adjustable seat and handlebars for comfort. Adjustable resistance systems; transport wheels, Supports up to 275 pounds; measures 20 x 46.5 x 48.5 inches (W x H x D).\",\"buyButton\":\"Shop Now\",\"buyButtonLink\":\"https://www.walmart.com/ip/Sunny-Health-Fitness-SF-B1203-Chain-Drive-Indoor-Cycling-Trainer-Exercise-Bike/21898525?u1=CN_i_-_OE_D_P_688785_605587563_no_refer&oid=499568.1&wmlspartner=Jv*v1/Wldzg&sourceid=12027443990518088950&affillinktype=10&veh=aff\",\"contentImage\":null,\"contentHtml\":\"<ul> <br/> <li>Walmart offers the <a rel=\\\"nofollow\\\" href=\\\"http://www.dealmoon.com/exec/j/?d=688785\\\" trk=\\\"Content-688785\\\" target=\\\"_blank\\\"> Sunny Health &amp; Fitness SF-B1203 Chain Drive Indoor Cycling Trainer Exercise Bike</a> for <strong>$99.96</strong>.</li> <br/> <li>Amazon has <a rel=\\\"nofollow\\\" href=\\\"http://www.dealmoon.com/exec/j/?d=688785&amp;url=https%3A%2F%2Fwww%2Eamazon%2Ecom%2FSunny%2DHealth%2DFitness%2DIndoor%2DCycling%2Fdp%2FB0090VYHMC%3FSubscriptionId%3D07PVPGV9KPJSRT8N9GG2%26tag%3Ddealmoon%2D20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D165953%26creativeASIN%3DB0090VYHMC\\\" trk=\\\"Content-688785\\\" target=\\\"_blank\\\">same price</a>.</li> <br/> <li>Free shipping.</li> <br/></ul> <br/><p>&nbsp;</p> <br/><p><strong>Features:&nbsp;</strong>Indoor cycling bike with 40-pound flywheel, Heavy-duty crank and smooth chain drive mechanism, Fully adjustable seat and handlebars for comfort. Adjustable resistance systems; transport wheels, Supports up to 275 pounds; measures 20 x 46.5 x 48.5 inches (W x H x D).</p>\",\"titleHtml\":\"<a target=\\\"_blank\\\" href=\\\"http://www.dealmoon.com/99-96-Sunny/688785.html\\\" title=\\\"\\\" class=\\\"event_statics_action\\\" statrk=\\\"view-688785-deal--\\\"> <span class=\\\"notice_item\\\"> $99.96 </span> <span>Sunny Health &amp; Fitness SF-B1203 Chain Drive Indoor Cycling Trainer Exercise Bike</span> </a>\",\"expired\":null,\"realTitle\":\"$99.96 Sunny Health & Fitness SF-B1203 Chain Drive Indoor Cycling Trainer Exercise Bike\",\"category\":\"Sports-Outdoors\",\"cate\":\"Sports & Outdoors\",\"docId\":\"114179.688785\"}";
//        JsonObject obj = new JsonParser().parse(s).getAsJsonObject();
//        System.out.println(obj.get("expired").equals(JsonNull.INSTANCE));
        System.out.println(new DealmoonGiftJsonExecutor().dealUrl("http://rover.ebay.com/rover/1/711-53200-19255-0/1?icep_ff3=2&pub=5574947394&toolid=10001&campid=5336777120&customid=&icep_item=222332268679&ipn=psmain&icep_vectorid=229466&kwid=902099&mtid=824&kw=lg"));
    }

}
