package college.com.collegenetwork.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

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

public class AddProfHandler implements View.OnClickListener, IWebResponseProcessor
{
    Context _context;

    @Override
    public void onClick(final View v )
    {
        _context = v.getContext();
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.addprofdialog, null);

        final AlertDialog.Builder builder =new AlertDialog.Builder(v.getContext());
        builder.setTitle("Add Professor");

        builder.setView(promptsView);

        final MyEditText profname = (MyEditText) promptsView.findViewById(R.id.profName);
//        final MyEditText profStream = (MyEditText) promptsView.findViewById(R.id.profStream);
        final MyEditText profEmail = (MyEditText) promptsView.findViewById(R.id.profEmail);
        final MyEditText profUserId = (MyEditText) promptsView.findViewById(R.id.userid);
        final MyEditText password = (MyEditText) promptsView.findViewById(R.id.password);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {

                JSONObject jsonBodyValues = new JSONObject();
                try
                {
                    jsonBodyValues.put("method","new");
                    jsonBodyValues.put("profName",profname.getText().toString());
                    jsonBodyValues.put("stream","professor");
                    jsonBodyValues.put("email",profEmail.getText().toString());
                    jsonBodyValues.put("userid",profUserId.getText().toString());
                    jsonBodyValues.put("pass",password.getText().toString());

                }catch (JSONException ex)
                {

                }
                new WebserviceProvider(ApplicationUrl.ADMIN_PROFESSORS, WebserviceProvider.RequestType.POST,AddProfHandler.this,jsonBodyValues).execute();
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
            String userMsg = ( (JSONObject) obj ).optString("msg");
            Toast.makeText(_context,userMsg,Toast.LENGTH_SHORT).show();
        }

    }

}
