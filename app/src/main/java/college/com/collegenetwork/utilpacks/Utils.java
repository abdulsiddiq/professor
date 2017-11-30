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

}
