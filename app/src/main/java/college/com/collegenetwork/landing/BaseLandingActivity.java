package college.com.collegenetwork.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import college.com.collegenetwork.R;
import college.com.collegenetwork.landing.fragments.MyApprovalFragment;
import college.com.collegenetwork.landing.fragments.MySectionFragment;
import college.com.collegenetwork.utilpacks.CriticalConstants;
import college.com.collegenetwork.utilpacks.FragmentNavigate;
import college.com.collegenetwork.utilpacks.MySharedPref;
import college.com.collegenetwork.utilpacks.MyTextView;

/**
 * Created by Krypto on 04-07-2017.
 */

public class BaseLandingActivity extends AppCompatActivity implements FragmentNavigate
{
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);

        boolean isStudent = new MySharedPref(this).getUserType() == CriticalConstants.STUDENT;

        MyTextView mySection = (MyTextView) findViewById(R.id.mysection);
        mySection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                showFragment(new MySectionFragment(), true);
            }
        });

        MyTextView myApprovals = (MyTextView) findViewById(R.id.approvals);

        if(isStudent)
        {
            myApprovals.setVisibility(View.GONE);
        }
        else
        {
            myApprovals.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    showFragment(new MyApprovalFragment(),true);
                }
            });
        }
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
