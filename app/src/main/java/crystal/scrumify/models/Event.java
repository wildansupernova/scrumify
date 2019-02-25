package crystal.scrumify.models;

public class Event {

    private int groupId;
    private String eventTime;

    public Event(String eventTime) {
        this.eventTime = eventTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

}
