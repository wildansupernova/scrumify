package crystal.scrumify.models;

public class ApiResponse<T> {

    private int status;
    private String statusMessage;
    private T data;

    public ApiResponse() {
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public T getData() {
        return data;
    }
}
