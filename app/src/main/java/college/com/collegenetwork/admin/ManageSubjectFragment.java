package college.com.collegenetwork.admin;

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
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class ManageSubjectFragment extends BaseFragment implements IWebResponseProcessor
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
            ArrayList<SubjectVO> subjectVos = new ArrayList<>();
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


            AdminSubjectAdapter adminSubAdapter = new AdminSubjectAdapter(getContext(),subjectVos);
            listView.setAdapter(adminSubAdapter);

        }
    }
}
