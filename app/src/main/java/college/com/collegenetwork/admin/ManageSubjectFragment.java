package college.com.collegenetwork.admin;

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
import college.com.collegenetwork.utilpacks.SubjectClickListener;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class ManageSubjectFragment extends BaseFragment implements IWebResponseProcessor
{
    RecyclerView listView;

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
        listView = (RecyclerView) view.findViewById(R.id.approval_list);
        getSubjects();
    }

    private void getSubjects()
    {
        JSONObject objects = new JSONObject();
        try
        {
            objects.put("method","fetch");
        }catch (JSONException ex)
        {

        }
        new WebserviceProvider(ApplicationUrl.ADMIN_SUBJECTS, WebserviceProvider.RequestType.POST,this,objects).execute();
    }


    @Override
    public void processResponse( Object obj, int status )
    {

        if (obj instanceof JSONObject)
        {
            JSONArray array = ( (JSONObject) obj ).optJSONArray("subjectList");
            final ArrayList<SubjectVO> subjectVos = new ArrayList<>();
            for(int i = 0; i< array.length();i++)
            {
                JSONObject object = array.optJSONObject(i);
                String subName = object.optString("name");
                String subProf = object.optString("prof");
                String subTiming = object.optString("timing");
                int subCap = object.optInt("capacity");
                String subStream = object.optString("stream");

                SubjectVO vo = new SubjectVO(subName,subProf,subTiming);
                vo.setCap(subCap);
                vo.setStream(subStream);
                subjectVos.add(vo);
            }


/*
            AdminSubjectAdapter adminSubAdapter = new AdminSubjectAdapter(getContext(),subjectVos);
            listView.setAdapter(adminSubAdapter);
*/
            SubjectListAdapter adapter = new SubjectListAdapter(subjectVos, new SubjectClickListener()
            {
                @Override
                public void onClickSubject( SubjectVO subjectVO )
                {
//                    Open Subject Edit Fragment
                    new SubjectEditHandler(subjectVO).onClick(listView);
                }
            });

            listView.setLayoutManager(new LinearLayoutManager(getContext()));
            listView.setAdapter(adapter);

        }
    }
}
