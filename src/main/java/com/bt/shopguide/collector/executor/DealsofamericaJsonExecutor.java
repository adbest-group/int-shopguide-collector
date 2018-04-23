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
public class DealsofamericaJsonExecutor extends AbstractJsonExecutor {
    private static Logger logger = LoggerFactory.getLogger(DealsofamericaJsonExecutor.class);

    /**样例
     {
     "taskId": 4061539104,
     "seedId": 114522,
     "createTime": "2017-09-28T10:23:16Z",
     "crawlTime": "2017-09-28T10:00:21Z",
     "url": "https://www.dealsofamerica.com/Finley-Home-Palazzo-Counter-Height-Dining-Table/399438.htm",
     "pid": "399438",
     "listImage": "https://jetimages.azureedge.net/md5/814675998c311b467c0ab752b39bc30a.100",
     "title": "Finley Home Palazzo Counter Height Dining Table",
     "realTitle": "Finley Home Palazzo Counter Height Dining Table for $170.98",
     "titleHtml": "<a target=\"_blank\" href=\"https://www.dealsofamerica.com/jump/jet.php?url=https://jet.com/product/Palazzo-Counter-Height-Dining-Table/a87d7caee9494b588d069c69cedebe35\"><span class=\"store-name\"><img alt=\"jet deals\" src=\"https://e4de9256dde2557498e1-90d59ea9c8ac0edbaf9cb0731dd6b930.ssl.cf2.rackcdn.com/jet-logo.gif\"></span></a><br/><h1 class=\"show_deals_head\">Finley Home Palazzo Counter Height Dining Table</h1> for <br/><span class=\"orange-caps\">$170.98</span><br/><br>",
     "currentPrice": "$170.98",
     "originalPrice": "$249.99",
     "mall": "Jet.com",
     "content": "The gleaming marble veneer tabletop of the Palazzo Counter Height Dining Table is more than just a pretty face: hard, durable, and stain-resistant, it's perfect for holding the beverages and snacks that go with any fun gathering, and cleans with an easy wipe. Extra-tall pub height is casual and comfortable in your kitchen or dining area, and fits pub chairs and stools.",
     "contentHtml": "<p>The gleaming marble veneer tabletop of the Palazzo Counter Height Dining Table is more than just a pretty face: hard, durable, and stain-resistant, it's perfect for holding the beverages and snacks that go with any fun gathering, and cleans with an easy wipe. Extra-tall pub height is casual and comfortable in your kitchen or dining area, and fits pub chairs and stools.</p>",
     "buyButton": "Shop now",
     "buyButtonLink": "https://jet.com/product/Palazzo-Counter-Height-Dining-Table/a87d7caee9494b588d069c69cedebe35?jcmp=afl:link:OfrhIMk*DmY:496283:1:10&siteID=OfrhIMk.DmY-aWlMEKP5hAfLgyny1cl4mA",
     "detailsPageImage": "https://jetimages.azureedge.net/md5/814675998c311b467c0ab752b39bc30a.100",
     "docId": "114522.399438"
     }
     **/

    public static String testJson = "{\n" +
            "    \"taskId\": 4061539104,\n" +
            "    \"seedId\": 114522,\n" +
            "    \"createTime\": \"2017-09-28T10:23:16Z\",\n" +
            "    \"crawlTime\": \"2017-09-28T10:00:21Z\",\n" +
            "    \"url\": \"https://www.dealsofamerica.com/Finley-Home-Palazzo-Counter-Height-Dining-Table/399438.htm\",\n" +
            "    \"pid\": \"399438\",\n" +
            "    \"listImage\": \"https://jetimages.azureedge.net/md5/814675998c311b467c0ab752b39bc30a.100\",\n" +
            "    \"title\": \"Finley Home Palazzo Counter Height Dining Table\",\n" +
            "    \"realTitle\": \"Finley Home Palazzo Counter Height Dining Table for $170.98\",\n" +
            "    \"titleHtml\": \"<a target=\\\"_blank\\\" href=\\\"https://www.dealsofamerica.com/jump/jet.php?url=https://jet.com/product/Palazzo-Counter-Height-Dining-Table/a87d7caee9494b588d069c69cedebe35\\\"><span class=\\\"store-name\\\"><img alt=\\\"jet deals\\\" src=\\\"https://e4de9256dde2557498e1-90d59ea9c8ac0edbaf9cb0731dd6b930.ssl.cf2.rackcdn.com/jet-logo.gif\\\"></span></a><br/><h1 class=\\\"show_deals_head\\\">Finley Home Palazzo Counter Height Dining Table</h1> for <br/><span class=\\\"orange-caps\\\">$170.98</span><br/><br>\",\n" +
            "    \"currentPrice\": \"$170.98\",\n" +
            "    \"originalPrice\": \"$249.99\",\n" +
            "    \"mall\": \"Jet.com\",\n" +
            "    \"content\": \"The gleaming marble veneer tabletop of the Palazzo Counter Height Dining Table is more than just a pretty face: hard, durable, and stain-resistant, it's perfect for holding the beverages and snacks that go with any fun gathering, and cleans with an easy wipe. Extra-tall pub height is casual and comfortable in your kitchen or dining area, and fits pub chairs and stools.\",\n" +
            "    \"contentHtml\": \"<p>The gleaming marble veneer tabletop of the Palazzo Counter Height Dining Table is more than just a pretty face: hard, durable, and stain-resistant, it's perfect for holding the beverages and snacks that go with any fun gathering, and cleans with an easy wipe. Extra-tall pub height is casual and comfortable in your kitchen or dining area, and fits pub chairs and stools.</p>\",\n" +
            "    \"buyButton\": \"Shop now\",\n" +
            "    \"buyButtonLink\": \"https://jet.com/product/Palazzo-Counter-Height-Dining-Table/a87d7caee9494b588d069c69cedebe35?jcmp=afl:link:OfrhIMk*DmY:496283:1:10&siteID=OfrhIMk.DmY-aWlMEKP5hAfLgyny1cl4mA\",\n" +
            "    \"detailsPageImage\": \"https://jetimages.azureedge.net/md5/814675998c311b467c0ab752b39bc30a.100\",\n" +
            "    \"docId\": \"114522.399438\"\n" +
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
            if(obj.get("buyButtonLink")==null || obj.get("buyButtonLink").equals(JsonNull.INSTANCE) || obj.get("buyButtonLink").getAsString().length()<1){
                return;
            }
            //检查缓存中是否标志已入库
            String pid = "int_shopguide_goods_dealsofamerica_"+obj.get("pid").getAsString();
            logger.info("good cache key:{}",new Object[]{pid});
            if(redisTemplate.opsForValue().get(pid)!=null){
                logger.info("dealsofamerica good-{} has collected!",new Object[]{obj.get("pid")});
                return;
            }
            glist = new GoodsList();
            gdetail = new GoodsDetail();
//            glist.setDiscounts(obj.get("discount").getAsString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            glist.setTitle(obj.get("title").getAsString());
            glist.setPrice((obj.get("currentPrice")==null || obj.get("currentPrice").equals(JsonNull.INSTANCE))?"":obj.get("currentPrice").getAsString());
            glist.setOriginalPrice((obj.get("originalPrice")==null || obj.get("originalPrice").equals(JsonNull.INSTANCE))?"":obj.get("originalPrice").getAsString());
            glist.setGoodSourceName("www.dealsofamerica.com");
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
