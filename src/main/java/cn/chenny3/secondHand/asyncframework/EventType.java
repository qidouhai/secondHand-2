package cn.chenny3.secondHand.asyncframework;

public enum EventType {
    COLLECT(1),HOT(2),VIEW(3);

    private int eventType;

    EventType(int eventType) {
        this.eventType = eventType;
    }


}
