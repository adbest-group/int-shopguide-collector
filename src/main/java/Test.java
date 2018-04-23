import com.bt.shopguide.collector.executor.DealmoonJsonExecutor;
import com.bt.shopguide.collector.executor.DealnewsJsonExecutor;
import com.bt.shopguide.collector.executor.DealsofamericaJsonExecutor;
import com.bt.shopguide.collector.executor.TechbargainsJsonExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caiting on 2017/9/11.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String[] cfgs = new String[]{"classpath:applicationContext.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(cfgs);
//
        ((DealmoonJsonExecutor)ctx.getBean("dealmoonJsonExecutor")).execute(DealmoonJsonExecutor.testJson);
//        ((TechbargainsJsonExecutor)ctx.getBean("techbargainsJsonExecutor")).execute(TechbargainsJsonExecutor.testJson);
//        ((DealnewsJsonExecutor)ctx.getBean("dealnewsJsonExecutor")).execute(DealnewsJsonExecutor.testJson);
//        ((DealsofamericaJsonExecutor)ctx.getBean("dealsofamericaJsonExecutor")).execute(DealsofamericaJsonExecutor.testJson);
        System.out.println("saved !");
    }

    public static String unicode(String source){
        StringBuffer sb = new StringBuffer();
        char [] source_char = source.toCharArray();
        String unicode = null;
        for (int i=0;i<source_char.length;i++) {
            unicode = Integer.toHexString(source_char[i]);
            if (unicode.length() <= 2) {
                unicode = "00" + unicode;
            }
            sb.append("\\u" + unicode);
        }
        System.out.println(sb);
        return sb.toString();
    }
    public static String decodeUnicode(String unicode) {
        StringBuffer sb = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            sb.append((char) data);
        }
        return sb.toString();
    }
}
