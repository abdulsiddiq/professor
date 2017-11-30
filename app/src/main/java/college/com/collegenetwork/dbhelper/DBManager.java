package college.com.collegenetwork.dbhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by sushantalone on 08/06/17.
 */

public class DBManager {

    private static DBManager _dbManager;
    private Object _lock;
    private SQLiteDatabase _database;

    private DBManager()
    {

    }

    private DBManager(Context context,ITableDetails[] tablesList)
    {
        MySqliteHelper _dbHelper = new MySqliteHelper(context, tablesList);
        _lock = new Object();
        _database = _dbHelper.getWritableDatabase();
    }

    public static void init(Context context,ITableDetails[] tablesList)
    {
          _dbManager = new DBManager(context, tablesList);
    }

    public static DBManager getInstance()
    {
        return _dbManager;
    }


    public void executeQuery(String query, IDBResultFetch resultFetcher)
    {
        synchronized (_lock)
        {
            Cursor cursor;
            try
            {
                cursor = _database.rawQuery(query,null);
                cursor = resultFetcher.fillData(cursor);
                cursor.close();

            }catch (SQLiteException sql)
            {
                    sql.printStackTrace();
            }
            finally {
            }
        }
    }

    public long executeQuery(CommandQuery query)
    {
        synchronized (_lock)
        {
            long result = -1;
            try
            {
                result = query.executeQuery(_database);

            }catch (SQLiteException sql)
            {
                    sql.printStackTrace();
            }
            finally {
                return result;
            }
        }
    }

    public void onExit()
    {
        _database.close();
    }

}
