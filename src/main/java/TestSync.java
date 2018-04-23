import com.bt.shopguide.collector.executor.DealmoonJsonExecutor;
import com.bt.shopguide.collector.util.HttpClientHelper;
import com.bt.shopguide.dao.entity.GoodsListWithHtml;
import com.bt.shopguide.dao.service.IGoodsListService;
import com.bt.shopguide.dao.vo.PageDataVo;
import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caiting on 2017/10/11.
 */
public class TestSync {
//    public static void main(String[] args) {
//        String[] cfgs = new String[]{"classpath:applicationContext.xml","classpath:applicationContext-mybatis.xml","classpath:applicationContext-redis.xml"};
//        ApplicationContext ctx = new ClassPathXmlApplicationContext(cfgs);
//
//
//        PageDataVo<GoodsListWithHtml> vo = new PageDataVo<>();
//        vo.setPageSize(5);
//        Map<String,Object> map = new HashMap<>();
//        map.put("id",Integer.valueOf(11343));
//        ((IGoodsListService)ctx.getBean("goodsListService")).selectGoodsListPageWithHtml(vo);
//        System.out.println(vo.getData().size());
//        Gson gson = new Gson();
//        Map<String,String> param = new HashMap<>();
//        param.put("json",gson.toJson(vo.getData()));
//        HttpClientHelper helper = HttpClientHelper.getInstance();
//        String content = helper.doPost("http://localhost:8081/api/sync/goods",param);
//        System.out.println(content);
//    }

    public static void main(String[] args) {
        String[] cfgs = new String[]{"classpath:applicationContext.xml","classpath:applicationContext-quartz.xml","classpath:applicationContext-mybatis.xml","classpath:applicationContext-redis.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(cfgs);
    }
}
