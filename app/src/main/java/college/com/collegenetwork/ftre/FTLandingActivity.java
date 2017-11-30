package college.com.collegenetwork.ftre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import college.com.collegenetwork.R;
import college.com.collegenetwork.utilpacks.FragmentNavigate;

/**
 * Created by Krypto on 03-07-2017.
 */

public class FTLandingActivity extends AppCompatActivity implements FragmentNavigate
{
    public static final String STREAM = "stream";

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftre_student);
        showFragment(new StreamChooseFragment(),false);
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
