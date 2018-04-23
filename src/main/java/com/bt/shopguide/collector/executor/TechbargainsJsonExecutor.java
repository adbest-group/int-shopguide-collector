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
 * Created by caiting on 2017/9/18.
 */
@Service
public class TechbargainsJsonExecutor extends AbstractJsonExecutor {
    private static Logger logger = LoggerFactory.getLogger(TechbargainsJsonExecutor.class);

    /**样例
     {
     "taskId": 4057574810,
     "seedId": 114160,
     "createTime": "2017-09-28T09:39:41Z",
     "crawlTime": "2017-09-28T09:39:44Z",
     "url": "https://www.techbargains.com/deal/445043/nebula-mars-portable-cinema",
     "pid": "445043",
     "listImage": "https://images.techbargains.com/pages/image.aspx?url=/datastore/userdeals/636420866143680000screenshot_17.png&width=200&height=200",
     "title": "Amazon Deal of the Day: Anker Nebula Mars 1280x800 3000lm JBL Speakers Portable Cinema $449",
     "currentPrice": "$449.00",
     "originalPrice": "$799.99",
     "discount": "$350.99 Off",
     "mall": "Amazon Coupons",
     "content": "EXPIRES TODAY Amazon Deal of the Day. Amazon has the Anker Nebula Mars 1280x800 3000lm JBL Speakers Portable Cinema for a low $449.00 Free Shipping. This retails for $800, so you are saving 44 % off. 3000lm LED lamp projects 500ANSI lumens of crisp 1280x800 picture JBL Dual 10W speakers flood any room with high-fidelity sound 19500 mAh battery with 3 hours of non-stop cinema Wireless cast to seamlessly stream content from iOS & Android  Cutting-edge DLP brings 3D support; 4k support",
     "buyButton": "see deal",
     "buyButtonLink": "https://zdbb.net/tb/?https%3a%2f%2fwww.amazon.com%2fgp%2fproduct%2fB073P3JHTH%2fref%3dox_sc_act_title_1%3fsmid%3dA294P4X9EWVXLJ%26tag%3dtbdirect-20%26ascsubtag%3d__GUID__&item_type=deal&page=https%3A%2F%2Fwww.techbargains.com%2Fjump%2Fdeal%2F445043&device=d&source=direct&item_id=445043&category=Home%2520Theater%2520%2526%2520TV%2520Accessories&vendor=Amazon&item_name=Amazon%2520Deal%2520of%2520the%2520Day%253A%2520Anker%2520Nebula%2520Mars%25201280x800%25203000lm%2520JBL%2520Speakers%2520Portable%2520Cinema%2520%2524449&original_name=Amazon%2520Deal%2520of%2520the%2520Day%253A%2520Nebula%2520Mars%25201280x800%25203000lm%2520JBL%2520Speakers%2520Portable%2520Cinema%2520%2524449&author=Ronnie%2520Langston&tracker=direct_undefined_Amazon_0928_445043&zd_ptax=436&click_attribution=deal_445043&price=%2520%2524449.00&clickGuid=__GUID__",
     "contentImage": "https://images.techbargains.com/pages/image.aspx?url=/datastore/userdeals/636420866143680000screenshot_17.png&width=200&height=200",
     "contentHtml": "<span class=\"expires\">EXPIRES TODAY</span> <br/><p><strong>Amazon Deal of the Day</strong>. Amazon has the <a href=\"/jump/link/145973?ref=D445043\" target=\"_blank\" rel=\"nofollow\" class=\"ga_out_deal_lnk\" data-store=\"Amazon\">Anker Nebula Mars 1280x800 3000lm JBL Speakers Portable Cinema</a> for a low <a href=\"/jump/link/145973?ref=D445043\" target=\"_blank\">$449.00 Free Shipping</a>. This retails for $800, so you are saving 44 % off.</p> <br/><ul> <br/> <li>3000lm LED lamp projects 500ANSI lumens of crisp 1280x800 picture</li> <br/> <li>JBL Dual 10W speakers flood any room with high-fidelity sound</li> <br/> <li>19500 mAh battery with 3 hours of non-stop cinema</li> <br/> <li>Wireless cast to seamlessly stream content from iOS &amp; Android&nbsp;</li> <br/> <li>Cutting-edge DLP brings 3D support; 4k support</li> <br/></ul>",
     "docId": "114160.445043"
     }
     **/

    public static String testJson = "{\n" +
            "    \"taskId\": 4057574810,\n" +
            "    \"seedId\": 114160,\n" +
            "    \"createTime\": \"2017-09-28T09:39:41Z\",\n" +
            "    \"crawlTime\": \"2017-09-28T09:39:44Z\",\n" +
            "    \"url\": \"https://www.techbargains.com/deal/445043/nebula-mars-portable-cinema\",\n" +
            "    \"pid\": \"445043\",\n" +
            "    \"listImage\": \"https://images.techbargains.com/pages/image.aspx?url=/datastore/userdeals/636420866143680000screenshot_17.png&width=200&height=200\",\n" +
            "    \"title\": \"Amazon Deal of the Day: Anker Nebula Mars 1280x800 3000lm JBL Speakers Portable Cinema $449\",\n" +
            "    \"currentPrice\": \"$449.00\",\n" +
            "    \"originalPrice\": \"$799.99\",\n" +
            "    \"discount\": \"$350.99 Off\",\n" +
            "    \"mall\": \"Amazon Coupons\",\n" +
            "    \"content\": \"EXPIRES TODAY Amazon Deal of the Day. Amazon has the Anker Nebula Mars 1280x800 3000lm JBL Speakers Portable Cinema for a low $449.00 Free Shipping. This retails for $800, so you are saving 44 % off. 3000lm LED lamp projects 500ANSI lumens of crisp 1280x800 picture JBL Dual 10W speakers flood any room with high-fidelity sound 19500 mAh battery with 3 hours of non-stop cinema Wireless cast to seamlessly stream content from iOS & Android  Cutting-edge DLP brings 3D support; 4k support\",\n" +
            "    \"buyButton\": \"see deal\",\n" +
            "    \"buyButtonLink\": \"https://zdbb.net/tb/?https%3a%2f%2fwww.amazon.com%2fgp%2fproduct%2fB073P3JHTH%2fref%3dox_sc_act_title_1%3fsmid%3dA294P4X9EWVXLJ%26tag%3dtbdirect-20%26ascsubtag%3d__GUID__&item_type=deal&page=https%3A%2F%2Fwww.techbargains.com%2Fjump%2Fdeal%2F445043&device=d&source=direct&item_id=445043&category=Home%2520Theater%2520%2526%2520TV%2520Accessories&vendor=Amazon&item_name=Amazon%2520Deal%2520of%2520the%2520Day%253A%2520Anker%2520Nebula%2520Mars%25201280x800%25203000lm%2520JBL%2520Speakers%2520Portable%2520Cinema%2520%2524449&original_name=Amazon%2520Deal%2520of%2520the%2520Day%253A%2520Nebula%2520Mars%25201280x800%25203000lm%2520JBL%2520Speakers%2520Portable%2520Cinema%2520%2524449&author=Ronnie%2520Langston&tracker=direct_undefined_Amazon_0928_445043&zd_ptax=436&click_attribution=deal_445043&price=%2520%2524449.00&clickGuid=__GUID__\",\n" +
            "    \"contentImage\": \"https://images.techbargains.com/pages/image.aspx?url=/datastore/userdeals/636420866143680000screenshot_17.png&width=200&height=200\",\n" +
            "    \"contentHtml\": \"<span class=\\\"expires\\\">EXPIRES TODAY</span> <br/><p><strong>Amazon Deal of the Day</strong>. Amazon has the <a href=\\\"/jump/link/145973?ref=D445043\\\" target=\\\"_blank\\\" rel=\\\"nofollow\\\" class=\\\"ga_out_deal_lnk\\\" data-store=\\\"Amazon\\\">Anker Nebula Mars 1280x800 3000lm JBL Speakers Portable Cinema</a> for a low <a href=\\\"/jump/link/145973?ref=D445043\\\" target=\\\"_blank\\\">$449.00 Free Shipping</a>. This retails for $800, so you are saving 44 % off.</p> <br/><ul> <br/> <li>3000lm LED lamp projects 500ANSI lumens of crisp 1280x800 picture</li> <br/> <li>JBL Dual 10W speakers flood any room with high-fidelity sound</li> <br/> <li>19500 mAh battery with 3 hours of non-stop cinema</li> <br/> <li>Wireless cast to seamlessly stream content from iOS &amp; Android&nbsp;</li> <br/> <li>Cutting-edge DLP brings 3D support; 4k support</li> <br/></ul>\",\n" +
            "    \"docId\": \"114160.445043\"\n" +
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
            String pid = "int_shopguide_goods_Techbargains_"+obj.get("pid").getAsString();
            logger.info("good cache key:{}",new Object[]{pid});
            if(redisTemplate.opsForValue().get(pid)!=null){
                logger.info("Techbargains good-{} has collected!",new Object[]{obj.get("pid")});
                return;
            }
            glist = new GoodsList();
            gdetail = new GoodsDetail();
            glist.setDiscounts((obj.get("discount")==null || obj.get("discount").equals(JsonNull.INSTANCE))?"":obj.get("discount").getAsString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            glist.setTitle(obj.get("title").getAsString());
            glist.setPrice((obj.get("currentPrice")==null || obj.get("currentPrice").equals(JsonNull.INSTANCE))?"":obj.get("currentPrice").getAsString());
            glist.setOriginalPrice((obj.get("originalPrice")==null || obj.get("originalPrice").equals(JsonNull.INSTANCE))?"":obj.get("originalPrice").getAsString());
            glist.setGoodSourceName("www.techbargains.com");
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
