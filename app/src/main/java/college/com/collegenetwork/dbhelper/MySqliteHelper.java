package college.com.collegenetwork.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sushantalone on 08/06/17.
 */

class MySqliteHelper extends SQLiteOpenHelper {

    private final String TAG = "SQLITEHelper";
    private static final String DATABASE_NAME = "orders.db";
    private static final int DATABASE_VERSION = 1;
    private ITableDetails[] _tables;

    MySqliteHelper(Context context,ITableDetails[] tablesList) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _tables = tablesList;
        Log.e(TAG,"constructor");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        for(ITableDetails table : _tables)
        {
            sql = table.getCreateQuery();
            db.execSQL(sql);
        }
        Log.e(TAG,"on create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(ITableDetails table : _tables)
        {
            if(table.getTableVersion() > oldVersion)
            {
                table.getUpdateQuery();
            }
        }
    }
}
