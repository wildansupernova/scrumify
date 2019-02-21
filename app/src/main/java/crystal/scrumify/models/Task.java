package crystal.scrumify.models;

import crystal.scrumify.utils.ConstantUtils;

public class Task {

    private String title;
    private String description;
    private String assignee;
    private int status;

    public Task(String title, String description, String assignee, int status) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean moveable() {
        return status == ConstantUtils.KANBAN_TODO_FRAG_ARG || status == ConstantUtils.KANBAN_PROGRESS_FRAG_ARG;
    }
}
