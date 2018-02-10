package cn.chenny3.secondHand.asyncframework;

import cn.chenny3.secondHand.common.utils.RedisKeyUtils;
import cn.chenny3.secondHand.common.utils.RedisUtils;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class EventProducer {
    @Autowired
    private RedisUtils redisUtils;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    public boolean fireEvent(EventModel eventModel) {
        Boolean flag=false;
        executor.execute(new EventProducerThread(flag,eventModel));
        return flag;

    }
    class EventProducerThread implements Runnable{
        private Boolean flag;
        private EventModel eventModel;

        public EventProducerThread(Boolean flag, EventModel eventModel) {
            this.flag = flag;
            this.eventModel = eventModel;
        }

        @Override
        public void run() {
            try {
                String json = SecondHandUtil.getJsonString(eventModel);
                redisUtils.lpush(RedisKeyUtils.getAsyncEventQueueKey(), json);
                flag=true;
            } catch (Exception e) {
                e.printStackTrace();
                flag=false;
            }
        }

        public Boolean getFlag() {
            return flag;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
        }

        public EventModel getEventModel() {
            return eventModel;
        }

        public void setEventModel(EventModel eventModel) {
            this.eventModel = eventModel;
        }


    }
}
