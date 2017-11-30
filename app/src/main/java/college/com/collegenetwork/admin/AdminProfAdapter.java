package college.com.collegenetwork.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.ProfessorVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class AdminProfAdapter extends BaseAdapter
{
    ArrayList<ProfessorVO> _subjects;
    Context _context;

    public AdminProfAdapter(Context context, ArrayList<ProfessorVO> professors)
    {
        _subjects = new ArrayList<>();
        _subjects = professors;
        _context = context;
    }

    @Override
    public int getCount() {
        return _subjects.size()+1;
    }

    @Override
    public ProfessorVO getItem(int position) {
        return _subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if(convertView == null)
        {
            holder = new ItemHolder();
            convertView = LayoutInflater.from(_context).inflate(R.layout.adminprofitem,null);
            holder.profName= (TextView) convertView.findViewById(R.id.profName);
            holder.profStream = (TextView) convertView.findViewById(R.id.profStream);
            holder.profEmail= (TextView) convertView.findViewById(R.id.profMail);
            holder.addBtn= (TextView) convertView.findViewById(R.id.newBtn);
            holder.editBtn= (TextView) convertView.findViewById(R.id.editBtn);
            holder.detailRow= (LinearLayout) convertView.findViewById(R.id.rowData);
            holder.deleteBtn = (TextView) convertView.findViewById(R.id.deleteBtn);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemHolder) convertView.getTag();
        }

        if (position == 0)
        {
            holder.detailRow.setVisibility(View.GONE);
            holder.addBtn.setVisibility(View.VISIBLE);

            holder.addBtn.setOnClickListener(new AddProfHandler());

        }
        else
        {
            holder.detailRow.setVisibility(View.VISIBLE);
            holder.addBtn.setVisibility(View.GONE);

            final ProfessorVO vo = getItem(position-1);

            holder.profName.setText(vo.getName());
            holder.profEmail.setText(vo.getEmail());
            holder.profStream.setText(vo.getStream());

            holder.editBtn.setOnClickListener(new EditProfHandler(vo));

            holder.deleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                    dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick( DialogInterface dialog, int which )
                        {
                            deleteProf(vo);
                        }
                    });
                    dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick( DialogInterface dialog, int which )
                        {
                            dialog.dismiss();
                        }
                    });
                    dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener()
                    {
                        @Override
                        public void onDismiss( DialogInterface dialog )
                        {
                        }
                    });
                    dialogBuilder.setMessage("Are you sure delete the Professor?");
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                }
            });
        }


        return convertView;
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

    private class ItemHolder
    {
        TextView profName;
        TextView profEmail;
        TextView profStream;
        TextView addBtn;
        TextView editBtn;
        TextView deleteBtn;
        LinearLayout detailRow;
    }
}
