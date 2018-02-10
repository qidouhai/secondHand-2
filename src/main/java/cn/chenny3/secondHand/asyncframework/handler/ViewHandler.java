package cn.chenny3.secondHand.asyncframework.handler;

import cn.chenny3.secondHand.asyncframework.EventHandler;
import cn.chenny3.secondHand.asyncframework.EventModel;
import cn.chenny3.secondHand.asyncframework.EventType;
import cn.chenny3.secondHand.common.utils.RedisKeyUtils;
import cn.chenny3.secondHand.common.utils.RedisUtils;
import cn.chenny3.secondHand.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 访问量处理器
 */
@Component
public class ViewHandler implements EventHandler{
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private GoodsService goodsService;

    @Override
    public List<EventType> getSupports() {
        return Arrays.asList(EventType.VIEW);
    }

    @Override
    public void doHandle(EventModel eventModel) {
        int goodsId=eventModel.getEntityid();
        //修改redis中的访问量
        redisUtils.incr(RedisKeyUtils.getGoodsViewKey(goodsId));
        //更新数据中的商品访问量
        goodsService.updateViewNum(goodsId, 1);
    }
}
