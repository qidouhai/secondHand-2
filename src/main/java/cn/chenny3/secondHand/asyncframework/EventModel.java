package cn.chenny3.secondHand.asyncframework;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    //事件类型
    private EventType eventType;
    //事件的触发者，一般是指请求的用户
    private int actionId;
    //事件关联的实体类型 ，例如 点赞事件，实体就是点赞的文章，entityId指文章Id,entityType指实体对应的类型
    private int entityid;
    private int entityType;
    //实体的拥有者
    private int eventOwnerId;
    //事件额外携带的数据
    private Map<String,Object> datas;

    public EventModel() {
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActionId() {
        return actionId;
    }

    public EventModel setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public int getEntityid() {
        return entityid;
    }

    public EventModel setEntityid(int entityid) {
        this.entityid = entityid;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEventOwnerId() {
        return eventOwnerId;
    }

    public EventModel setEventOwnerId(int eventOwnerId) {
        this.eventOwnerId = eventOwnerId;
        return this;
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public EventModel setDatas(Map<String, Object> datas) {
        this.datas = datas;
        return this;
    }

    public EventModel setDatas(){
        this.datas=new HashMap<String, Object>();
        return this;
    }

    public EventModel setData(String key,Object value){
        datas.put(key, value);
        return this;
    }

}
