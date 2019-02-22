package crystal.scrumify.responses;

import crystal.scrumify.models.User;

public class LoginResponse {

    private User data;
    private String statusMessage;

    public LoginResponse() {
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
