package college.com.collegenetwork.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.ProfessorVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.MyEditText;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class EditProfHandler implements View.OnClickListener, IWebResponseProcessor
{
    Context _context;

    ProfessorVO _vo;
    public EditProfHandler( ProfessorVO vo )

    {
     _vo = vo;
    }


    @Override
    public void onClick(final View v )
    {
        _context = v.getContext();
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.addprofdialog, null);

        final AlertDialog.Builder builder =new AlertDialog.Builder(v.getContext());
        builder.setTitle("Edit Professor");

        builder.setView(promptsView);

        final MyEditText profname = (MyEditText) promptsView.findViewById(R.id.profName);
//        final MyEditText profStream = (MyEditText) promptsView.findViewById(R.id.profStream);
        final MyEditText profEmail = (MyEditText) promptsView.findViewById(R.id.profEmail);
        final MyEditText profUserId = (MyEditText) promptsView.findViewById(R.id.userid);
        final MyEditText password = (MyEditText) promptsView.findViewById(R.id.password);
        final Button deleteProf = (Button) promptsView.findViewById(R.id.deleteProf);


        deleteProf.setVisibility(View.VISIBLE);
        deleteProf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                deleteProf(_vo);
            }
        });

        profUserId.setVisibility(View.GONE);
        password.setVisibility(View.GONE);

        profname.setText(_vo.getName());
//        profStream.setText(_vo.getStream());
        profEmail.setText(_vo.getEmail());

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {

                JSONObject jsonBodyValues = new JSONObject();
                try
                {
                    jsonBodyValues.put("method","edit");
                    jsonBodyValues.put("profName",profname.getText().toString());
                    jsonBodyValues.put("stream","professor");
                    jsonBodyValues.put("email",profEmail.getText().toString());
                    jsonBodyValues.put("prevprofName",_vo.getName());
                    jsonBodyValues.put("prevstream",_vo.getStream());
                    jsonBodyValues.put("prevemail",_vo.getEmail());

                }catch (JSONException ex)
                {

                }
                new WebserviceProvider(ApplicationUrl.ADMIN_PROFESSORS, WebserviceProvider.RequestType.POST,EditProfHandler.this,jsonBodyValues).execute();
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


    private void deleteProf( ProfessorVO _vo)
    {
        JSONObject jsonBodyValues = new JSONObject();
        try
        {
            jsonBodyValues.put("method","delete");
            jsonBodyValues.put("prevprofName",_vo.getName());
            jsonBodyValues.put("prevstream",_vo.getStream());
            jsonBodyValues.put("prevemail",_vo.getEmail());

        }catch (JSONException ex)
        {

        }
        new WebserviceProvider(ApplicationUrl.ADMIN_PROFESSORS, WebserviceProvider.RequestType.POST, new IWebResponseProcessor()
        {
            @Override
            public void processResponse( Object obj, int status )
            {
//                TODO : Do Nothing
            }
        }, jsonBodyValues).execute();

    }
}
