package crystal.scrumify.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import crystal.scrumify.R;
import crystal.scrumify.activities.KanbanActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_proactive)
                .setContentTitle("Daily Meeting!")
                .setContentText("You Have Daily Meeting in 10 Minutes")
                .setTicker("Alert! Daily Meeting")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
