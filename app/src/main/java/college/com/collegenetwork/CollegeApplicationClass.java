package college.com.collegenetwork;

import android.app.Application;

import college.com.collegenetwork.dbhelper.DBManager;
import college.com.collegenetwork.dbhelper.ITableDetails;
import college.com.collegenetwork.dbtables.UserTable;

/**
 * Created by Krypto on 20-06-2017.
 */

public class CollegeApplicationClass extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        DBManager.init(getApplicationContext(),new ITableDetails[]{new UserTable()});
    }
}
