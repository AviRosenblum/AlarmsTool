package bin.ellie.com.alarmstool;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;


/*  receiver for fire alarms from SQLite database when boot completed and also
 *  to get the pending intent and send notification.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // fire alarm when boot completed
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SetAlarms.fireAlarms(context);
        } else {
            // send notification when pending intent arrive
            NotificationManager mNM;
            // open main activity when clicking notification
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("isFromAlarm", true);
            // pending intent for the notification
            PendingIntent pIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), i, 0);
            // create notification object
            mNM = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification mNotification = new Notification.Builder(context)
                    .setContentTitle("")
                    .setContentText(intent.getStringExtra("content"))
                    .setStyle(new Notification.BigTextStyle().bigText(intent.getStringExtra("content")))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{0, 1000})
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIntent).setAutoCancel(true).build();
            // send notification
            mNM.notify((int)System.currentTimeMillis(), mNotification);
            // set state of the alarm to off after it sent to the user.
            SetAlarms.setAlarmOff(context, intent.getStringExtra("id"));
        }
    }
}
