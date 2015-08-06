package bin.ellie.com.alarmstool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/** This class is for firing alarm after phone is re-booted.
 *  all functions in this class are static and require Context for the database class.
 *  Created by Avi Rozenblum.
 */
public class SetAlarms {

    private static final int ON = 1;

    /* use whenever create new alarm
    parameters:
    context - context.
    time - long variable converted to string with the time to notify
    id - the id of the pending intent
    content - the content of the message.
    */
    public static void addAlarmToSQL(Context context, String time, String id, String content) {
        SQLiteDatabase sdb = new DBAlarms(context).getWritableDatabase();
        int x = ON;
        sdb.execSQL("INSERT INTO alarms (id, content, time, state) VALUES (?, ?, ?, ?)",
                new Object[]{id, content, time, x});
    }

    /* use whenever alarm is sent and needs to be in off state
    parameters:
    context - context.
    id - the id of the original pending intent
    */
    public static void setAlarmOff(Context context, String id) {
        SQLiteDatabase sdb = new DBAlarms(context).getWritableDatabase();
        sdb.execSQL("UPDATE alarms SET state = 0 WHERE id = '" + id + "'");
    }

//    use when boot completed and alarms needs to be fire again. that will fire only alarms with state set to ON
//    parameters: context.
    public static void fireAlarms(Context context) {
        SQLiteDatabase sdb = new DBAlarms(context).getWritableDatabase();
        Cursor cursor = sdb.rawQuery("SELECT * FROM alarms", null);
        int size = cursor.getCount();
        if (size > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                // check if alarm state is ON
                if (cursor.getInt(3) == ON) {
                    long firstTime = Long.valueOf(cursor.getString(2));
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    intent.putExtra("content", cursor.getString(1));
                    intent.putExtra("id", cursor.getString(0));
                    PendingIntent mAlarmSender = PendingIntent.getBroadcast(context, 0, intent, 0);
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);
                }
            }
        }
    }

//    use to delete all alarms
//    parameters: context.
    public static void deleteOffAlarms(Context context) {
        SQLiteDatabase sdb = new DBAlarms(context).getWritableDatabase();
        sdb.execSQL("DELETE FROM alarms WHERE state =0");
    }
}
