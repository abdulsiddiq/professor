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
import college.com.collegenetwork.models.ProfessorVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class ManageProfessorsFragment extends BaseFragment implements IWebResponseProcessor
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
        new WebserviceProvider(ApplicationUrl.ADMIN_PROFESSORS, WebserviceProvider.RequestType.POST,this,objects).execute();
    }


    @Override
    public void processResponse( Object obj, int status )
    {

        if (obj instanceof JSONObject)
        {
            JSONArray array = ( (JSONObject) obj ).optJSONArray("proflist");
            ArrayList<ProfessorVO> professorVOS = new ArrayList<>();
            for(int i = 0; i< array.length();i++)
            {
                JSONObject object = array.optJSONObject(i);
                String fullName = object.optString("name");
                String email = object.optString("mail");
                String stream = object.optString("stream");

                ProfessorVO vo = new ProfessorVO();
                vo.setName(fullName);
                vo.setEmail(email);
                vo.setStream(stream);

                professorVOS.add(vo);
            }


            AdminProfAdapter adminProfAdapter = new AdminProfAdapter(getContext(),professorVOS);
            listView.setAdapter(adminProfAdapter);

        }
    }

}
