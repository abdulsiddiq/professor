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
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class AdminSubjectAdapter extends BaseAdapter
{
    ArrayList<SubjectVO> _subjects;
    Context _context;

    public AdminSubjectAdapter(Context context, ArrayList<SubjectVO> professors)
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
    public SubjectVO getItem(int position) {
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
            convertView = LayoutInflater.from(_context).inflate(R.layout.adminsubitem,null);
            holder.subName = (TextView) convertView.findViewById(R.id.subName);
            holder.subCap = (TextView) convertView.findViewById(R.id.totalCap);
            holder.subProf = (TextView) convertView.findViewById(R.id.profName);
            holder.subVenue = (TextView) convertView.findViewById(R.id.venue);
            holder.subStream = (TextView) convertView.findViewById(R.id.subStream);

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

            holder.addBtn.setOnClickListener(new NewSubjectHandler());

        }
        else
        {
            holder.detailRow.setVisibility(View.VISIBLE);
            holder.addBtn.setVisibility(View.GONE);

            final SubjectVO vo = getItem(position-1);

            holder.subName.setText(vo.getSubName());
            holder.subStream.setText(vo.getStream());
            holder.subProf.setText(vo.getProf());
            holder.subVenue.setText(vo.getTiming());
            holder.subCap.setText("cap : "+vo.getCap());


            holder.editBtn.setOnClickListener(new SubjectEditHandler(vo));

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
                            deleteSubject(vo);
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
                    dialogBuilder.setMessage("Are you sure delete the Subject?");
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                }
            });
    }


        return convertView;
    }


    private void deleteSubject( SubjectVO _vo)
    {
        JSONObject jsonBodyValues = new JSONObject();
        try
        {
            jsonBodyValues.put("method","delete");
            jsonBodyValues.put("prevsubname",_vo.getSubName());
            jsonBodyValues.put("prevstream",_vo.getStream());
            jsonBodyValues.put("prevsubprof",_vo.getProf());
            jsonBodyValues.put("prevtiming",_vo.getTiming());
            jsonBodyValues.put("prevcap",_vo.getCap());

        }catch (JSONException ex)
        {

        }
        new WebserviceProvider(ApplicationUrl.ADMIN_SUBJECTS, WebserviceProvider.RequestType.POST, new IWebResponseProcessor()
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
        TextView subName;
        TextView subProf;
        TextView subStream;
        TextView subVenue;
        TextView subCap;
        TextView addBtn;
        TextView editBtn;
        TextView deleteBtn;
        LinearLayout detailRow;
    }

}
