package cn.chenny3.secondHand.asyncframework;

import java.util.List;

public interface EventHandler {
    /**
     * 获取处理器支持的事件类型
     * @return
     */
    List<EventType> getSupports();

    /**
     * 处理事件的核心方法
     * @param eventModel
     */
    void doHandle(EventModel eventModel);
}
