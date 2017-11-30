package college.com.collegenetwork.utilpacks;

import android.content.Context;
import android.content.SharedPreferences;

import college.com.collegenetwork.R;

/**
 * Created by Krypto on 03-07-2017.
 */

public class MySharedPref
{
    private SharedPreferences _sharedPref;
    private final String USER_REGISTERED = "userReg";
    private final String USER_TYPE = "usertype";
    private final String USERNAME = "username";
    private final String FULL_NAME = "fullname";
    private final String EMAIL = "email";
    private final String SUB_SELECT = "sub_selected";

    public MySharedPref( Context context )
    {
        if(context != null)
        {
            _sharedPref = context.getSharedPreferences(context.getString(R.string.pref_name), Context.MODE_PRIVATE);
        }
    }

    public void setUserRegistered( boolean isRegistered ,String userName, String fullName, String email)
    {
        SharedPreferences.Editor editor = _sharedPref.edit();
        editor.putBoolean(USER_REGISTERED , isRegistered);
        editor.putString(USERNAME, userName);
        editor.putString(FULL_NAME, fullName);
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public boolean isUserRegistered()
    {
        return _sharedPref.getBoolean(USER_REGISTERED, false);
    }

    public void setUserType(int userType)
    {
        SharedPreferences.Editor editor = _sharedPref.edit();
        editor.putInt(USER_TYPE , userType);
        editor.commit();
    }

    public void subjectTourCompleted()
    {
        SharedPreferences.Editor editor = _sharedPref.edit();
        editor.putBoolean(SUB_SELECT , true);
        editor.commit();
    }

    public int getUserType()
    {
        return _sharedPref.getInt(USER_TYPE, CriticalConstants.STUDENT);
    }

    public String getUserId()
    {
        return _sharedPref.getString(USERNAME,"");
    }

    public String getFullName()
    {
        return _sharedPref.getString(FULL_NAME,"");
    }

    public boolean isSubSelectComplete()
    {
        return _sharedPref.getBoolean(SUB_SELECT,false);
    }
}
