package college.com.collegenetwork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import college.com.collegenetwork.admin.AdminBaseFragment;
import college.com.collegenetwork.utilpacks.FragmentNavigate;

/**
 * Created by Krypto on 02-11-2017.
 */

public class AdminLandingActivity extends AppCompatActivity implements FragmentNavigate
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_base);
        showFragment(new AdminBaseFragment(),false);
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
