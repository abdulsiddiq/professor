package college.com.collegenetwork.utilpacks;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Krypto on 03-07-2017.
 */

public class BaseFragment extends Fragment
{
    protected FragmentNavigate _navigate;

    @Override
    public void onAttach( Context context )
    {
        super.onAttach(context);
        _navigate = (FragmentNavigate) context;
    }
}
