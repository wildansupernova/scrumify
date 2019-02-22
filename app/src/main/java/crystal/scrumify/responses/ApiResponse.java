package crystal.scrumify.responses;

public class ApiResponse<T> {

    private String statusMessage;
    private T data;

    public ApiResponse() {
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
