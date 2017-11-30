package college.com.collegenetwork.ftre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import college.com.collegenetwork.R;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-07-2017.
 */

public class StreamChooseFragment extends BaseFragment implements IWebResponseProcessor
{
    RadioGroup _streamGroup;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.streamlayout,container,false);
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);
        fetchStream();
        _streamGroup = (RadioGroup) view.findViewById(R.id.streamGroup);
        _streamGroup.check(0);
        getActivity().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int checkIndex = _streamGroup.getCheckedRadioButtonId()-1;
                if(checkIndex != -1)
                {
                    CharSequence stream = ( (RadioButton) _streamGroup.getChildAt(checkIndex) ).getText();
                    ChooseSubjectsFragment fragment = new ChooseSubjectsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(FTLandingActivity.STREAM, stream.toString());
                    fragment.setArguments(bundle);
                    _navigate.showFragment(fragment, false);
                }
            }
        });
    }

    private void fetchStream()
    {
        new WebserviceProvider(ApplicationUrl.LISTFETCH, WebserviceProvider.RequestType.POST,this).execute();
    }

    @Override
    public void processResponse( Object obj, int status )
    {
        JSONObject object = (JSONObject) obj;
        JSONArray streamArray = object.optJSONArray("streamList");
        for(int i = 0;i < streamArray.length();i++)
        {
            final RadioButton btn = new RadioButton(getContext());
            btn.setText(streamArray.optString(i));
            _streamGroup.addView(btn);
        }
    }
}
