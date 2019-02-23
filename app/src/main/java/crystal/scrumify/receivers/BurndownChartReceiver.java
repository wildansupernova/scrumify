package crystal.scrumify.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import crystal.scrumify.R;
import crystal.scrumify.activities.BurndownChartActivity;
import crystal.scrumify.models.CustomDataEntry;


public class BurndownChartReceiver extends BroadcastReceiver {

    private long PERIOD = 1000;
    private static int counter = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BurndownChartActivity.getInstance() != null) {
            Context ctx = BurndownChartActivity.getInstance();
            int y = (int) (Math.random() * 5);
            BurndownChartActivity.getInstance().addData(y);
            CustomDataEntry entry = new CustomDataEntry(String.valueOf(counter++), y, y+1, y+2);

            Gson gson = new Gson();
            String jsonString = gson.toJson(entry);


            InputStream is = context.getResources().openRawResource(R.raw.burtdown_chart_data);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (Exception e) {

            } finally {

            }


        }
    }

    public void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BurndownChartReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + PERIOD, PERIOD, pendingIntent);
        Log.d(BurndownChartReceiver.class.getSimpleName(), "alarm setted");
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BurndownChartReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(sender);
    }
}
