package cn.chenny3.secondHand.asyncframework;

import cn.chenny3.secondHand.common.utils.RedisKeyUtils;
import cn.chenny3.secondHand.common.utils.RedisUtils;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;
    //事件类型和支持的事件处理器的关联映射
    private Map<EventType, List<EventHandler>> mappings=new HashMap<>();
    @Autowired
    private RedisUtils redisUtils;
    //线程池
    private ThreadPoolExecutor threadPoolExecutor;

    {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //从Spring ioc中获取已经注册了EventHandler实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        //遍历
        for (String key : beans.keySet()) {
            EventHandler eventHandler = beans.get(key);
            //将支持同一个事件类型的处理器组合在一起，形成链路
            for (EventType eventType : eventHandler.getSupports()) {
                if (mappings.containsKey(eventType)) {
                    mappings.get(eventType).add(eventHandler);
                } else {
                    ArrayList<EventHandler> eventHandlers = new ArrayList<>();
                    eventHandlers.add(eventHandler);
                    mappings.put(eventType, eventHandlers);
                }
            }
        }

        //working
        doConsume();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 消费者从redis阻塞队列中读取事件的方法
     */
    public void doConsume() {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        //redis brpop
                        String json = redisUtils.brpop(RedisKeyUtils.getAsyncEventQueueKey());
                        //反序列化json
                        EventModel eventModel = SecondHandUtil.getObjectFromJson(json, EventModel.class);
                        //根据映射获取支持处理此事件类型的处理器链
                        List<EventHandler> handlers = mappings.get(eventModel.getEventType());
                        for (EventHandler eventHandler : handlers) {
                            //依次调用处理器链的处理方法
                            eventHandler.doHandle(eventModel);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
