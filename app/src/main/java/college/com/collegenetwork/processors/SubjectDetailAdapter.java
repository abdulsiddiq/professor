package college.com.collegenetwork.processors;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.admin.SubjectEditHandler;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 02-12-2017.
 */

public class SubjectDetailAdapter extends RecyclerView.Adapter<SubjectDetailAdapter.ItemHolder>
{

    ArrayList<SubjectVO> subjectVOS;
    public SubjectDetailAdapter( ArrayList<SubjectVO> subjectVOS)
    {
        this.subjectVOS = subjectVOS;
    }

    @Override
    public ItemHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_detail,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position )
    {
        holder.onBind(position);
    }

    @Override
    public int getItemCount()
    {
        return this.subjectVOS.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder
    {
        TextView subName;
        TextView subProf;
        TextView subStream;
        TextView subVenue;
        TextView subCap;
        TextView editBtn;
        TextView deleteBtn;
        LinearLayout detailRow;

        public ItemHolder( View itemView )
        {
            super(itemView);
            subName = (TextView) itemView.findViewById(R.id.subName);
            subCap = (TextView) itemView.findViewById(R.id.totalCap);
            subProf = (TextView) itemView.findViewById(R.id.profName);
            subVenue = (TextView) itemView.findViewById(R.id.venue);
            subStream = (TextView) itemView.findViewById(R.id.subStream);

            editBtn= (TextView) itemView.findViewById(R.id.editBtn);
            detailRow= (LinearLayout) itemView.findViewById(R.id.rowData);
            deleteBtn = (TextView) itemView.findViewById(R.id.deleteBtn);
        }

        public void onBind(int position)
        {

            final SubjectVO vo = subjectVOS.get(position);

            detailRow.setVisibility(View.VISIBLE);

            subName.setText(vo.getSubName());
           subStream.setText(vo.getStream());
            subProf.setText(vo.getProf());
            subVenue.setText(vo.getTiming());
            subCap.setText("cap : "+vo.getCap());


            editBtn.setOnClickListener(new SubjectEditHandler(vo));

            deleteBtn.setOnClickListener(new View.OnClickListener()
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

}
