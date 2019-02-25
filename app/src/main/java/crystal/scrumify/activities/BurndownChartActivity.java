package crystal.scrumify.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
import crystal.scrumify.responses.ApiResponse;
import crystal.scrumify.responses.ChartValue;
import crystal.scrumify.services.ApiService;
import crystal.scrumify.utils.PreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BurndownChartActivity extends AppCompatActivity {

    AnyChartView burndownChart;
    Cartesian cartesian;
    List<DataEntry> seriesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burdown_chart);

        burndownChart = findViewById(R.id.burndown_chart);
        cartesian = AnyChart.line();

        setupCartesian();

        seriesData = new ArrayList<>();
        addSeriesData();
    }


    private void addSeriesData() {
        ApiService.getApi().getChartValue(PreferenceUtils.getGroupId()).enqueue(new Callback<ApiResponse<List<ChartValue>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ChartValue>>> call, Response<ApiResponse<List<ChartValue>>> response) {
                if (response.isSuccessful()) {
                    for (ChartValue chartValue : response.body().getData()) {
                        System.out.println(chartValue.getX());
                        System.out.println(chartValue.getValue());
                        seriesData.add(new CustomDataEntry(chartValue));
                        seriesData.add(new CustomDataEntry(new ChartValue("2019-02-26", 87)));
                    }

                    fillChart();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ChartValue>>> call, Throwable t) {

            }
        });
    }

    private void fillChart() {
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

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

        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        burndownChart.setChart(cartesian);
    }

    private void setupCartesian() {
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");
        cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(ChartValue chartValue) {
            super(chartValue.getX(), chartValue.getValue());
        }
    }

}
