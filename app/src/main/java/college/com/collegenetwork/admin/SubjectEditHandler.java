package college.com.collegenetwork.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.MyEditText;
import college.com.collegenetwork.utilpacks.Utils;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class SubjectEditHandler implements View.OnClickListener , IWebResponseProcessor
{
    Context _context;
    SubjectVO _vo;
    public SubjectEditHandler(SubjectVO vo)
    {
        _vo = vo;
    }

    @Override
    public void onClick(final View v )
    {
        _context = v.getContext();
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.addsubdialog, null);

        final AlertDialog.Builder builder =new AlertDialog.Builder(v.getContext());
        builder.setTitle("Edit Subject");

        builder.setView(promptsView);

        final MyEditText subname = (MyEditText) promptsView.findViewById(R.id.subName);
        final MyEditText subStream = (MyEditText) promptsView.findViewById(R.id.subStream);
        final Spinner subProf = (Spinner) promptsView.findViewById(R.id.prof_name_spinner);
        final Spinner date = (Spinner) promptsView.findViewById(R.id.dayspinner);
        final Spinner hour = (Spinner) promptsView.findViewById(R.id.hour_spinner);
        final Spinner minutes = (Spinner) promptsView.findViewById(R.id.minute_spinner);

        final MyEditText subCapacity = (MyEditText) promptsView.findViewById(R.id.capacity);

        Utils.getProfs(_context,subProf,_vo.getProf());

        initTiming(date,hour,minutes);


        subname.setText(_vo.getSubName());
        subStream.setText(_vo.getStream());
        subCapacity.setText(_vo.getCap()+"");


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {

                JSONObject jsonBodyValues = new JSONObject();
                try
                {
                    jsonBodyValues.put("method","edit");
                    jsonBodyValues.put("subname",subname.getText().toString());
                    jsonBodyValues.put("stream",subStream.getText().toString());
                    jsonBodyValues.put("subprof",subProf.getSelectedItem());
                    jsonBodyValues.put("timing",Utils.getTiming(date,hour,minutes));
                    jsonBodyValues.put("cap",subCapacity.getText().toString());
                    jsonBodyValues.put("prevsubname",_vo.getSubName());
                    jsonBodyValues.put("prevstream",_vo.getStream());
                    jsonBodyValues.put("prevsubprof",_vo.getProf());
                    jsonBodyValues.put("prevtiming",_vo.getTiming());
                    jsonBodyValues.put("prevcap",_vo.getCap());

                }catch (JSONException ex)
                {

                }
                new WebserviceProvider(ApplicationUrl.ADMIN_SUBJECTS, WebserviceProvider.RequestType.POST,SubjectEditHandler.this,jsonBodyValues).execute();
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

    private void initTiming( Spinner date, Spinner hour, Spinner minutes )
    {
        String[] timing = _vo.getTiming().split(",");

        ArrayAdapter dateAdapter = new ArrayAdapter(_context, android.R.layout.simple_spinner_item, Utils.DAYS);
        ArrayAdapter hourAdapter = new ArrayAdapter(_context, android.R.layout.simple_spinner_item, Utils.HOURS);
        ArrayAdapter minutesAdapter = new ArrayAdapter(_context, android.R.layout.simple_spinner_item, Utils.MINUTES);

        date.setAdapter(dateAdapter);
        hour.setAdapter(hourAdapter);
        minutes.setAdapter(minutesAdapter);

        date.setSelection(Utils.getIndex(Utils.DAYS,timing[0]));
        hour.setSelection(Utils.getIndex(Utils.HOURS,timing[1]));
        minutes.setSelection(Utils.getIndex(Utils.MINUTES,timing[2]));

    }

    @Override
    public void processResponse( Object obj, int status )
    {
        if(obj instanceof JSONObject)
        {

        }

    }

}
