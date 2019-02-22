package crystal.scrumify.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.models.CustomDataEntry;
import crystal.scrumify.receivers.BurtdownChartReceiver;
import crystal.scrumify.utils.ConstantUtils;

public class BurtdownChartActivity extends BaseActivity {

    private AnyChartView burdownChart;
    private Cartesian cartesian;
    List<DataEntry> seriesData = new ArrayList<>();

    private static BurtdownChartActivity instance = null;
    private static int counter = 1;

    public BurtdownChartActivity() {
        super(R.layout.activity_burdown_chart);
    }

    public static BurtdownChartActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (instance == null) {
            instance = this;
        }
    }

    @Override
    public void bindView() {
        burdownChart = findViewById(R.id.burdown_chart);
        cartesian = AnyChart.line();
    }

    private void fillChart() {
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Brandy");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Whiskey");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Tequila");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        burdownChart.setChart(cartesian);
    }

    public void startService(View view) {
        BurtdownChartReceiver burtdownChartReceiver = new BurtdownChartReceiver();
        burtdownChartReceiver.setAlarm(this);
        Toast.makeText(this, "Timer 1 menit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setupView() {
        cartesian.animation(true);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title("Burdown Chart");
        cartesian.yAxis(0).title("Left Story Points");
        cartesian.xAxis(0).title("Day");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
    }

    public void addData(int y) {
        seriesData.add(new CustomDataEntry(String.valueOf(counter), y+1, y+2, y+3));
        counter++;
        fillChart();
        Log.d(BurtdownChartActivity.class.getSimpleName(), "addData " + String.valueOf(y));
    }
}
