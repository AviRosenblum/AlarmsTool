package bin.ellie.com.alarmstool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBAlarms extends SQLiteOpenHelper {

    public DBAlarms(Context context) {
        super(context, "alarm", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE alarms (id TEXT, content TEXT, time TEXT, state INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
