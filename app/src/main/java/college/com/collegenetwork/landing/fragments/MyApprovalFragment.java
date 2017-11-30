package college.com.collegenetwork.landing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.landing.adapters.ApprovalAdapter;
import college.com.collegenetwork.models.ApprovalVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.utilpacks.MySharedPref;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 24-08-2017.
 */

public class MyApprovalFragment extends BaseFragment implements IWebResponseProcessor
{
    ListView listView;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.approval_screen,container,false);
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.approval_list);
        getApprovals();
    }

    private void getApprovals()
    {
        JSONObject jsonBody = new JSONObject();
        try
        {
            jsonBody.put("prof", new MySharedPref(getContext()).getFullName());
        }catch (Exception ex)
        {

        }
         new WebserviceProvider(ApplicationUrl.MYAPPROVALS, WebserviceProvider.RequestType.POST,this,jsonBody).execute();
    }


    @Override
    public void processResponse( Object obj, int status )
    {

        if (obj instanceof JSONObject)
        {
            JSONObject object = (JSONObject) obj;

            JSONArray array = object.optJSONArray("subjectList");
            ArrayList<ApprovalVO> approvalList = new ArrayList<>();
            for (int index = 0; index < array.length(); index++)
            {
                JSONObject subObj = array.optJSONObject(index);
                String studentName = subObj.optString("student");
                String subject = subObj.optString("subject");
                String timing = subObj.optString("timing");
                String stream = subObj.optString("stream");
                ApprovalVO subjectVO = new ApprovalVO(studentName, timing, subject,stream);
                approvalList.add(subjectVO);
            }

            approvalList.add(new ApprovalVO("sidique","friday","android","computer"));

            ApprovalAdapter adapter = new ApprovalAdapter(getContext(), approvalList);
            listView.setAdapter(adapter);
        }
    }

}
