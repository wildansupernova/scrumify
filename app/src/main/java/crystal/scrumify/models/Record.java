package crystal.scrumify.models;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record {

    private String recordTitle;
    private String recorder;
    private String dateCreated;

    @SuppressLint("NewApi")
    public Record(String recordTitle, String recorder) {
        this.recordTitle = recordTitle;
        this.recorder = recorder;
        this.dateCreated = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:MM:SS").format(LocalDateTime.now());
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getDateCreated() {
        return dateCreated;
    }
}
