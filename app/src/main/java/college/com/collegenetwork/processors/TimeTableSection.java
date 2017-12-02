package college.com.collegenetwork.processors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.utilpacks.MySharedPref;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 02-12-2017.
 */

public class TimeTableSection extends BaseFragment implements IWebResponseProcessor
{

    RecyclerView _listView;

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
        _listView = (RecyclerView) view.findViewById(R.id.approval_list);
        _listView.setLayoutManager(new LinearLayoutManager(getContext()));
        populateTimetable();
    }

    private void populateTimetable()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {

            jsonObject.put("username", new MySharedPref(getContext()).getFullName());
        }catch (JSONException ex)
        {
            ex.printStackTrace();
        }

        new WebserviceProvider(ApplicationUrl.PROF_TT, WebserviceProvider.RequestType.POST,this,jsonObject).execute();
    }

    @Override
    public void processResponse(Object obj, int status) {

        if(obj instanceof  JSONObject)
        {
            JSONObject object = (JSONObject) obj;

                JSONArray array = object.optJSONArray("subjectslist");
                ArrayList<SubjectVO> updatedSubjectList = new ArrayList<>();
                for(int index = 0; index < array.length();index++)
                {
                    JSONObject subObj = array.optJSONObject(index);
                    String subjectname = subObj.optString("subjectName");
                    String subTiming = subObj.optString("timing");
                    String subStream = subObj.optString("subjectStream");
                    SubjectVO subjectVO =  new SubjectVO(subjectname,new MySharedPref(getContext()).getFullName(),subTiming);
                    subjectVO.setStream(subStream);
                    updatedSubjectList.add(subjectVO);
                }

/*
                StudentSectionAdapter adapter = new StudentSectionAdapter(getContext(),updatedSubjectList);
                _sectionList.setAdapter(adapter);
*/

            TimeTableSectionAdapter adapter = new TimeTableSectionAdapter(updatedSubjectList);
            _listView.setAdapter(adapter);
        }
    }

}
