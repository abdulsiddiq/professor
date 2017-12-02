package college.com.collegenetwork.utilpacks;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.ProfessorVO;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 31-05-2017.
 */

public class Utils
{
    //Email Validation pattern
    public static final String regEx = "\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\b";

    public static void showToast( String message, Context context)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }



    public final static String[] DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    public final static String[] HOURS = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
    public final static String[] MINUTES = {"00","05","10","15","20","25","30","35","40","45","50","55","60"};

    public final static String COMMA = ",";




    public static void getProfs( final Context context, final Spinner chooser)
    {
        JSONObject objects = new JSONObject();
        try
        {
            objects.put("method","fetch");
        }catch (JSONException ex)
        {

        }
        new WebserviceProvider(ApplicationUrl.ADMIN_PROFESSORS, WebserviceProvider.RequestType.POST, new IWebResponseProcessor()
        {
            @Override
            public void processResponse( Object obj, int status )
            {
//                Add professors to the chooser list
                if (obj instanceof JSONObject)
                {
                    JSONArray array = ( (JSONObject) obj ).optJSONArray("proflist");
                    ArrayList<ProfessorVO> professorVOS = new ArrayList<>();
                    ArrayList<String> profNames = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject object = array.optJSONObject(i);
                        String fullName = object.optString("name");
                        profNames.add(fullName);
                        String email = object.optString("mail");
                        String stream = object.optString("stream");

                        ProfessorVO vo = new ProfessorVO();
                        vo.setName(fullName);
                        vo.setEmail(email);
                        vo.setStream(stream);

                        professorVOS.add(vo);
                    }

                    ArrayAdapter aa = new ArrayAdapter(context, R.layout.spinner_item, profNames);
                    chooser.setAdapter(aa);
                }
            }
        }, objects).execute();
    }

    public static void getProfs( final Context context, final Spinner chooser, final String highlightProf)
    {
        JSONObject objects = new JSONObject();
        try
        {
            objects.put("method","fetch");
        }catch (JSONException ex)
        {

        }
        new WebserviceProvider(ApplicationUrl.ADMIN_PROFESSORS, WebserviceProvider.RequestType.POST, new IWebResponseProcessor()
        {
            @Override
            public void processResponse( Object obj, int status )
            {
//                Add professors to the chooser list
                if (obj instanceof JSONObject)
                {
                    JSONArray array = ( (JSONObject) obj ).optJSONArray("proflist");
                    ArrayList<ProfessorVO> professorVOS = new ArrayList<>();
                    ArrayList<String> profNames = new ArrayList<>();
                    int index = 0;
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject object = array.optJSONObject(i);
                        String fullName = object.optString("name");
                        profNames.add(fullName);

                        if(highlightProf.equalsIgnoreCase(fullName))
                        {
                            index = i;
                        }
                        String email = object.optString("mail");
                        String stream = object.optString("stream");

                        ProfessorVO vo = new ProfessorVO();
                        vo.setName(fullName);
                        vo.setEmail(email);
                        vo.setStream(stream);

                        professorVOS.add(vo);
                    }

                    ArrayAdapter aa = new ArrayAdapter(context, R.layout.spinner_item, profNames);
                    chooser.setAdapter(aa);
                    chooser.setSelection(index);
                }
            }
        }, objects).execute();
    }



    public static int getIndex(String[] collection, String item)
    {
        for(int ind = 0; ind < collection.length; ind++)
        {
            if(collection[ind].equalsIgnoreCase(item))
            {
                return ind;
            }
        }
        return 0;
    }


    public static String getTiming( Spinner date, Spinner hour, Spinner minutes )
    {
        StringBuilder builder = new StringBuilder();
        builder.append(date.getSelectedItem())
                .append(Utils.COMMA)
                .append(hour.getSelectedItem())
                .append(Utils.COMMA)
                .append(minutes.getSelectedItem());
        return builder.toString();
    }

}
