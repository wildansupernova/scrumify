package crystal.scrumify.responses;

import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.google.gson.annotations.SerializedName;

public class ChartValue {

    @SerializedName("tanggal")
    private String x;

    @SerializedName("score_sisa")
    private int value;

    public ChartValue(String x, int value) {
        this.x = x;
        this.value = value;
    }

    public String getX() {
        return x;
    }

    public int getValue() {
        return value;
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
