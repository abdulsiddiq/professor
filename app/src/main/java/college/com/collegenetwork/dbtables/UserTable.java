package college.com.collegenetwork.dbtables;

import college.com.collegenetwork.dbhelper.ITableDetails;

/**
 * Created by Krypto on 20-06-2017.
 */

public class UserTable implements ITableDetails
{
    final int dbVersion = 1;

    public static final String TABLE_USER = "user_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLOUMN_NAME = "user_name";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_USER
            + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLOUMN_NAME + " TEXT)";
    @Override
    public int getTableVersion()
    {
        return dbVersion;
    }

    @Override
    public String getCreateQuery()
    {
        return null;
    }

    @Override
    public String getUpdateQuery()
    {
        return null;
    }
}