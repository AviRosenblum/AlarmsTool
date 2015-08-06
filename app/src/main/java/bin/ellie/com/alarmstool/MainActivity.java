package bin.ellie.com.alarmstool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void notify(View v){
        // create intent for notification
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        String message = "";
        intent.putExtra("content", message);
        String id = "" + (int) System.currentTimeMillis();;
        intent.putExtra("id", id);
        // create pending intent for notification
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(id), intent, 0);
        // set alarm manager object to fire in 5 minutes from now
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        time = System.currentTimeMillis() + 300000;
        am.set(AlarmManager.RTC_WAKEUP, time, mAlarmSender);
        // add this alarm to the database to fire it again when phone re-booted
        SetAlarms.addAlarmToSQL(getApplicationContext(), String.valueOf(time), id, message);
    }

}
