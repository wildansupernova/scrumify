package crystal.scrumify.models;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record {

    private String recordTitle;

    public Record(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }
}
