package cn.chenny3.secondHand.asyncframework;

import cn.chenny3.secondHand.common.utils.RedisKeyUtils;
import cn.chenny3.secondHand.common.utils.RedisUtils;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    private RedisUtils redisUtils;
    public boolean fireEvent(EventModel eventModel){
        try{
            String json = SecondHandUtil.getJsonString(eventModel);
            redisUtils.lpush(RedisKeyUtils.getAsyncEventQueueKey(),json);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
