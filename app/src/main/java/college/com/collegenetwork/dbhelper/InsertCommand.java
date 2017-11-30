package college.com.collegenetwork.dbhelper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sushantalone on 08/06/17.
 */

public class InsertCommand implements CommandQuery {

    private String _tableName;
    private ContentValues _contentValues;

    public InsertCommand(String tableName)
    {
        _tableName = tableName;
        _contentValues = new ContentValues();
    }

    public void addValue(String key, int value)
    {
        _contentValues.put(key,value);
    }

    public void addValue(String key, String value)
    {
        _contentValues.put(key,value);
    }

    public void addValue(String key, long value)
    {
        _contentValues.put(key,value);
    }
    public void addValue(String key, Double value)
    {
        _contentValues.put(key,value);
    }
    public void addValue(String key, float value)
    {
        _contentValues.put(key,value);
    }

    public void addValue(String key, boolean value)
    {
        _contentValues.put(key,value);
    }

    @Override
    public long executeQuery(SQLiteDatabase db) {
        return db.insert(_tableName, null, _contentValues);
    }
}
