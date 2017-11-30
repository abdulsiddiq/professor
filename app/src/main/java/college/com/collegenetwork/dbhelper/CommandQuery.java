package college.com.collegenetwork.dbhelper;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sushantalone on 08/06/17.
 */

public interface CommandQuery {

    long executeQuery( SQLiteDatabase db );
}
