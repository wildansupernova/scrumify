package crystal.scrumify.responses;

import com.google.gson.annotations.SerializedName;

public class TaskResponse {

    @SerializedName("id")
    private int taskId;

    @SerializedName("group_id")
    private int groupId;

    @SerializedName("task_name")
    private String taskName;

    @SerializedName("description")
    private String description;

    @SerializedName("kanban_status")
    private String kanbanStatus;

    @SerializedName("work_hour")
    private int workHour;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("assignee")
    private String assignee;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKanbanStatus() {
        return kanbanStatus;
    }

    public void setKanbanStatus(String kanbanStatus) {
        this.kanbanStatus = kanbanStatus;
    }

    public int getWorkHour() {
        return workHour;
    }

    public void setWorkHour(int workHour) {
        this.workHour = workHour;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssignee() {
        return assignee;
    }

    public boolean moveable() {
        return kanbanStatus.equals("OPEN") || kanbanStatus.equals("WIP");
    }
}
