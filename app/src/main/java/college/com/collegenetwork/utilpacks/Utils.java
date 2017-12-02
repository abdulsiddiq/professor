package college.com.collegenetwork.utilpacks;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Krypto on 31-05-2017.
 */

public class Utils
{
    //Email Validation pattern
    public static final String regEx = "\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\b";

    public static void showToast( String message, Context context)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }



    public final static String[] DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    public final static String[] HOURS = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
    public final static String[] MINUTES = {"00","05","10","15","20","25","30","35","40","45","50","55","60"};

}
