package college.com.collegenetwork.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import college.com.collegenetwork.R;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.MyEditText;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class NewSubjectHandler implements View.OnClickListener, IWebResponseProcessor
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
        final MyEditText subProf = (MyEditText) promptsView.findViewById(R.id.subProf);
        final MyEditText subTiming = (MyEditText) promptsView.findViewById(R.id.timing);
        final MyEditText subCapacity = (MyEditText) promptsView.findViewById(R.id.capacity);


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
                    jsonBodyValues.put("subprof",subProf.getText().toString());
                    jsonBodyValues.put("timing",subTiming.getText().toString());
                    jsonBodyValues.put("cap",subCapacity.getText().toString());

                }catch (JSONException ex)
                {

                }
                new WebserviceProvider(ApplicationUrl.ADMIN_SUBJECTS, WebserviceProvider.RequestType.POST,NewSubjectHandler.this,jsonBodyValues).execute();
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

    @Override
    public void processResponse( Object obj, int status )
    {
        if(obj instanceof JSONObject)
        {

        }

    }

}
