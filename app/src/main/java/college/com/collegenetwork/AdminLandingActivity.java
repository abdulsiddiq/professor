package college.com.collegenetwork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import college.com.collegenetwork.admin.AdminBaseFragment;
import college.com.collegenetwork.utilpacks.FragmentNavigate;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 02-11-2017.
 */

public class AdminLandingActivity extends AppCompatActivity implements IWebResponseProcessor, View.OnClickListener,FragmentNavigate
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_base);
        showFragment(new AdminBaseFragment(),true);
    }


    @Override
    public void processResponse( Object obj, int status )
    {
        Log.d("MyActivity", "status = " + status +" instance of = " + obj.getClass().getName());
    }

    @Override
    public void onClick( View v )
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("testing","value");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        new WebserviceProvider(getResources().getString(R.string.url), WebserviceProvider.RequestType.POST,this,jsonObject).execute();
    }

    @Override
    public void showFragment( Fragment fragmentToShow, boolean addToBackStack)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if(addToBackStack)
        {
            ft.add(R.id.main_container, fragmentToShow);
            ft.addToBackStack(null);
        }
        else
        {
            ft.replace(R.id.main_container,fragmentToShow);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


}
