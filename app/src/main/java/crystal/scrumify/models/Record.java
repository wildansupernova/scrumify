package crystal.scrumify.models;

public class Record {

    private String record_title;
    private String recorder;

    public Record(String record_title, String recorder) {
        this.record_title = record_title;
        this.recorder = recorder;
    }

    public String getRecord_title() {
        return record_title;
    }

    public void setRecord_title(String record_title) {
        this.record_title = record_title;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }
}
