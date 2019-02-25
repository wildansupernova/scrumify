package crystal.scrumify.models;

public class Event {

    private int groupId;
    private String eventName;
    private String eventTime;

    public Event(String eventName, String eventTime) {
        this.eventName = eventName;
        this.eventTime = eventTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

}
