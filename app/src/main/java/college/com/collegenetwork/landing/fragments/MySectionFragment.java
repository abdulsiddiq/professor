package college.com.collegenetwork.landing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.landing.adapters.FacultySectionAdapter;
import college.com.collegenetwork.landing.adapters.StudentSectionAdapter;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.utilpacks.CriticalConstants;
import college.com.collegenetwork.utilpacks.MySharedPref;
import college.com.collegenetwork.utilpacks.Utils;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 20-08-2017.
 */

public class MySectionFragment extends BaseFragment implements IWebResponseProcessor {
    ListView _sectionList;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.mysection,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _sectionList = (ListView) view.findViewById(R.id.section_list);
        populateEnrolledSubjects();

    }

    private void populateEnrolledSubjects()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            int userType = new MySharedPref(getContext()).getUserType();
            if(userType == CriticalConstants.STUDENT)
            {
                jsonObject.put("username", new MySharedPref(getContext()).getUserId());
            }
            else
            {
                jsonObject.put("username", new MySharedPref(getContext()).getFullName());
            }
            jsonObject.put("usertype", new MySharedPref(getContext()).getUserType());
        }catch (JSONException ex)
        {
            ex.printStackTrace();
        }
//        ((BaseActivity) getActivity()).startFTRActivity();

        new WebserviceProvider(ApplicationUrl.MYSECTION, WebserviceProvider.RequestType.POST,this,jsonObject).execute();
    }

    @Override
    public void processResponse(Object obj, int status) {

        if(obj instanceof  JSONObject)
        {
            JSONObject object = (JSONObject) obj;

            int userType = new MySharedPref(getContext()).getUserType();

            if(userType == CriticalConstants.STUDENT)
            {

                JSONArray array = object.optJSONArray("subjectList");
                ArrayList<SubjectVO> updatedSubjectList = new ArrayList<>();
                for(int index = 0; index < array.length();index++)
                {
                    JSONObject subObj = array.optJSONObject(index);
                    String subjectname = subObj.optString("subjectName");
                    String subjectProf = subObj.optString("prof");
                    String subTiming = subObj.optString("timing");
                    SubjectVO subjectVO =  new SubjectVO(subjectname,subjectProf,subTiming);
                    updatedSubjectList.add(subjectVO);
                }

                StudentSectionAdapter adapter = new StudentSectionAdapter(getContext(),updatedSubjectList);
                _sectionList.setAdapter(adapter);
            }
            else
            {
                JSONArray array = object.optJSONArray("studentList");
                ArrayList<SubjectVO> updatedSubjectList = new ArrayList<>();
                for(int index = 0; index < array.length();index++)
                {
                    JSONObject subObj = array.optJSONObject(index);
                    String subjectname = subObj.optString("subjectName");
                    String studentName = subObj.optString("student");
                    String subTiming = subObj.optString("timing");
                    SubjectVO subjectVO =  new SubjectVO(subjectname,studentName,subTiming);
                    updatedSubjectList.add(subjectVO);
                }
                if(updatedSubjectList.size() == 0)
                {
                    Utils.showToast("No Students Have Enrolled",getContext());
                }
                else
                {
                    FacultySectionAdapter adapter = new FacultySectionAdapter(getContext(), updatedSubjectList);
                    _sectionList.setAdapter(adapter);
                }
            }
        }
    }
}