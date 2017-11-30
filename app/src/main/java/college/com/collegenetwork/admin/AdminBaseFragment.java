package college.com.collegenetwork.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import college.com.collegenetwork.R;
import college.com.collegenetwork.utilpacks.BaseFragment;

/**
 * Created by Krypto on 12-11-2017.
 */

public class AdminBaseFragment extends BaseFragment
{
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.admin_base_frag,container,false);
    }


    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.subjects).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                _navigate.showFragment(new ManageSubjectFragment(),true);
            }
        });
        view.findViewById(R.id.professors).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                _navigate.showFragment(new ManageProfessorsFragment(),true);
            }
        });


    }


}
