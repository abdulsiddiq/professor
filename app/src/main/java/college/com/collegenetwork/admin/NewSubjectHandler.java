package college.com.collegenetwork.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.ProfessorVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.MyEditText;
import college.com.collegenetwork.utilpacks.Utils;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class NewSubjectHandler implements View.OnClickListener
{
    Context _context;

    @Override
    public void onClick(final View v )
    {
        _context = v.getContext();
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.addsubdialog, null);

        final AlertDialog.Builder builder =new AlertDialog.Builder(v.getContext());
        builder.setTitle("Add Subject");

        builder.setView(promptsView);

        final MyEditText subname = (MyEditText) promptsView.findViewById(R.id.subName);
        final MyEditText subStream = (MyEditText) promptsView.findViewById(R.id.subStream);
        final Spinner subProf = (Spinner) promptsView.findViewById(R.id.prof_name_spinner);
        final Spinner date = (Spinner) promptsView.findViewById(R.id.dayspinner);
        final Spinner hour = (Spinner) promptsView.findViewById(R.id.hour_spinner);
        final Spinner minutes = (Spinner) promptsView.findViewById(R.id.minute_spinner);

        initTimeSpinners(date,hour,minutes);

        final MyEditText subCapacity = (MyEditText) promptsView.findViewById(R.id.capacity);


        getProfs(subProf);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {

                JSONObject jsonBodyValues = new JSONObject();
                try
                {
                    jsonBodyValues.put("method","new");
                    jsonBodyValues.put("subname",subname.getText().toString());
                    jsonBodyValues.put("stream",subStream.getText().toString());
                    jsonBodyValues.put("subprof",subProf.getSelectedItem());
                    jsonBodyValues.put("timing",getTiming(date,hour,minutes));
                    jsonBodyValues.put("cap",subCapacity.getText().toString());

                }catch (JSONException ex)
                {

                }
                new WebserviceProvider(ApplicationUrl.ADMIN_SUBJECTS, WebserviceProvider.RequestType.POST, new IWebResponseProcessor()
                {
                    @Override
                    public void processResponse( Object obj, int status )
                    {

                    }
                }, jsonBodyValues).execute();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void initTimeSpinners( Spinner date, Spinner hour, Spinner minutes )
    {
        ArrayAdapter dateAdapter = new ArrayAdapter(_context, android.R.layout.simple_spinner_item, Utils.DAYS);
        ArrayAdapter hourAdapter = new ArrayAdapter(_context, android.R.layout.simple_spinner_item, Utils.HOURS);
        ArrayAdapter minutesAdapter = new ArrayAdapter(_context, android.R.layout.simple_spinner_item, Utils.MINUTES);

        date.setAdapter(dateAdapter);
        hour.setAdapter(hourAdapter);
        minutes.setAdapter(minutesAdapter);



    }

    private String getTiming( Spinner date, Spinner hour, Spinner minutes )
    {
        StringBuilder builder = new StringBuilder();
        builder.append(date.getSelectedItem())
                .append(hour.getSelectedItem())
                .append(minutes.getSelectedItem());
        return builder.toString();
    }

    private void getProfs( final Spinner chooser)
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

                    ArrayAdapter aa = new ArrayAdapter(_context, R.layout.spinner_item, profNames);
                    chooser.setAdapter(aa);
                }
            }
        }, objects).execute();
    }

}
