import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by caiting on 2017/9/11.
 */
public class TestListener implements MessageListener {

    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        System.out.println(msg);

    }
}
