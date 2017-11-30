package college.com.collegenetwork.authpack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import college.com.collegenetwork.BaseActivity;
import college.com.collegenetwork.R;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.utilpacks.CriticalConstants;
import college.com.collegenetwork.utilpacks.MyEditText;
import college.com.collegenetwork.utilpacks.MySharedPref;
import college.com.collegenetwork.utilpacks.Utils;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-07-2017.
 */

public class LoginFragment extends BaseFragment implements IWebResponseProcessor
{
    MyEditText _username , _password;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.activity_signin, container, false);
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);
        _username = (MyEditText) view.findViewById(R.id.usrusr);
        _password = (MyEditText) view.findViewById(R.id.pswrd);

        view.findViewById(R.id.sin).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                login();
            }
        });
    }

    private void login()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("username",_username.getText().toString());
            jsonObject.put("pass",_password.getText().toString());
        }catch (JSONException ex)
        {
            ex.printStackTrace();
        }
//        ((BaseActivity) getActivity()).AdminLandingAcitivity();

        new WebserviceProvider(ApplicationUrl.LOGIN, WebserviceProvider.RequestType.POST,this,jsonObject).execute();
    }

    @Override
    public void processResponse( Object obj, int status )
    {
        if(obj instanceof JSONObject)
        {
            JSONObject data = (JSONObject) obj;
            int result = data.optInt("success");
            if(result == CriticalConstants.SUCCESS)
            {
//                Proceed with login
                MySharedPref mySharedPref = new MySharedPref(getContext());
                mySharedPref.setUserType(Integer.valueOf(data.optString("usertype")));
                MySharedPref sharedPref = new MySharedPref(getContext());
                sharedPref.setUserRegistered(true,data.optString("user"),data.optString("fname"),data.optString("email"));

                if(sharedPref.getUserType() == CriticalConstants.STUDENT)
                {
                    if (!sharedPref.isSubSelectComplete())
                    {
                        ( (BaseActivity) getActivity() ).startFTRActivity();
                    } else
                    {
                        ( (BaseActivity) getActivity() ).BaseLandingActivity();
                    }
                }
                else if(sharedPref.getUserType() == CriticalConstants.PROFESSOR)
                {
                    ( (BaseActivity) getActivity() ).BaseLandingActivity();
                }
                else
                {
                    ( (BaseActivity) getActivity() ).AdminLandingAcitivity();
                }
            }
            else
            {
                Utils.showToast("Wrong Credentials",getContext());
            }
        }
        else
        {
            Utils.showToast("Something went wrong! Please check your internet connection",getContext());
        }
    }


}
