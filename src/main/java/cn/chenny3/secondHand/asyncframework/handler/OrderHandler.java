package cn.chenny3.secondHand.asyncframework.handler;

import cn.chenny3.secondHand.asyncframework.EventHandler;
import cn.chenny3.secondHand.asyncframework.EventModel;
import cn.chenny3.secondHand.asyncframework.EventType;

import java.util.List;

public class OrderHandler implements EventHandler{
    @Override
    public List<EventType> getSupports() {
        return null;
    }

    @Override
    public synchronized void doHandle(EventModel eventModel) {

    }
}
