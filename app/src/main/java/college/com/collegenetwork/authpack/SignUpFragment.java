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
import college.com.collegenetwork.utilpacks.MyTextView;
import college.com.collegenetwork.utilpacks.Utils;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-07-2017.
 */

public class SignUpFragment extends BaseFragment implements IWebResponseProcessor
{
    MyEditText _fullName,_email,_userName,_passWord,_confirmPass;
    MyTextView _create, _skipToLogin;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.activity_signup, container,false);
    }


    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);
        _fullName = (MyEditText) view.findViewById(R.id.fname);
        _email = (MyEditText) view.findViewById(R.id.mail);
        _userName = (MyEditText) view.findViewById(R.id.usrusr);
        _passWord = (MyEditText) view.findViewById(R.id.pswrd);
        _confirmPass = (MyEditText) view.findViewById(R.id.c_pswrd);

        _create = (MyTextView) view.findViewById(R.id.create);
        _skipToLogin = (MyTextView) view.findViewById(R.id.skip);

        _create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                checkAndCreate();
            }
        });

        _skipToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                _navigate.showFragment(new LoginFragment(),true);
            }
        });

    }


    private void checkAndCreate()
    {
        String messageToShow="";
        if(_userName.getText().toString().isEmpty())
        {
            messageToShow += "Username :";
        }
        if(_fullName.getText().toString().isEmpty())
        {
            messageToShow += "fullName :";
        }
        if(_email.getText().toString().isEmpty())
        {
            messageToShow += "email :";
        }

        if(messageToShow.isEmpty())
        {
            String passWord = _passWord.getText().toString();
            if(passWord.isEmpty())
            {
                Utils.showToast("Password Cannot be empty",getContext());
            }
            else
            {
                if(passWord.equals(_confirmPass.getText().toString()))
                {
                    registerUser();
                }
                else
                {
                    Utils.showToast("PassWord Do Not Match",getContext());
                }
            }
        }
        else
        {
            Utils.showToast("Please fill the fields" + messageToShow,getContext());
        }

    }

    private void registerUser()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("fname",_fullName.getText().toString());
            jsonObject.put("email",_email.getText().toString());
            jsonObject.put("username",_userName.getText().toString());
            jsonObject.put("password",_passWord.getText().toString());
            jsonObject.put("type", CriticalConstants.STUDENT);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        new WebserviceProvider(ApplicationUrl.REGISTER, WebserviceProvider.RequestType.POST,this,jsonObject).execute();
        Utils.showToast("service hit for registering user",getContext());

    }

    @Override
    public void processResponse( Object obj, int status )
    {
        if(obj instanceof JSONObject)
        {
            JSONObject result = (JSONObject) obj;
            int success = result.optInt("success");
            if(success == 200)
            {
//                Registration Success
                ((BaseActivity) getActivity()).startFTRActivity();
                MySharedPref sharedPref = new MySharedPref(getContext());
                sharedPref.setUserRegistered(true,result.optString("username"),result.optString("fname"),result.optString("email"));
                sharedPref.setUserType(CriticalConstants.STUDENT);
            }
            else
            {
//                Retry
                Utils.showToast("Username already taken",getContext());
            }
        }
    }
}
