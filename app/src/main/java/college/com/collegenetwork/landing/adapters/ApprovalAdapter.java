package college.com.collegenetwork.landing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.ApprovalVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.CriticalConstants;
import college.com.collegenetwork.utilpacks.MySharedPref;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 28-08-2017.
 */

public class ApprovalAdapter  extends RecyclerView.Adapter<ApprovalAdapter.ItemHolder>
{

    ArrayList<ApprovalVO> _approvalVOs;
    Context _context;
    public ApprovalAdapter( Context context , ArrayList<ApprovalVO> approvalVOs )
    {
        _context = context;
        _approvalVOs = approvalVOs;
    }

    @Override
    public ItemHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        View convertView = LayoutInflater.from(_context).inflate(R.layout.student_section_item,null);
        return new ItemHolder(convertView);
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position )
    {

        holder.onBind(_approvalVOs.get(position));
    }

    @Override
    public int getItemCount()
    {
        return _approvalVOs.size();
    }


    private void callAprroveAPI( boolean approve, final ApprovalVO vo)
    {
        JSONObject object = new JSONObject();
        try
        {
            MySharedPref pref = new MySharedPref(_context);
            object.put("prof",pref.getFullName());
            object.put("student",vo.getStudentName());
            object.put("venue",vo.getTiming());
            object.put("subname",vo.getSubject());
            object.put("stream",vo.getStream());
            object.put("isApproved",approve ? CriticalConstants.APPROVE : CriticalConstants.REJECT);

        }catch (JSONException ex)
        {
        }
        new WebserviceProvider(ApplicationUrl.CHANGE_APPROVAL, WebserviceProvider.RequestType.POST, new IWebResponseProcessor()
        {
            @Override
            public void processResponse( Object obj, int status )
            {
                if(obj instanceof JSONObject)
                {
                    _approvalVOs.remove(vo);
                    ApprovalAdapter.this.notifyDataSetChanged();
                }
            }
        }, object).execute();
    }

    class ItemHolder extends RecyclerView.ViewHolder
    {
        TextView subjectName;
        TextView studentName;
        TextView venue;
        TextView approveBtn;
        TextView rejectBtn;

        public ItemHolder( View convertView)
        {
            super(convertView);
            studentName = (TextView) convertView.findViewById(R.id.sub_name);
            subjectName = (TextView) convertView.findViewById(R.id.prof_name);
            venue= (TextView) convertView.findViewById(R.id.venue);
            approveBtn= (TextView) convertView.findViewById(R.id.approveBtn);
            rejectBtn= (TextView) convertView.findViewById(R.id.rejectBtn);
            approveBtn.setVisibility(View.VISIBLE);
            rejectBtn.setVisibility(View.VISIBLE);
       }

       public void onBind(final ApprovalVO vo)
       {
           subjectName.setText(vo.getSubject());
           studentName.setText(vo.getStudentName());
           venue.setText(vo.getTiming());

           approveBtn.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick( View v )
               {

                   callAprroveAPI(true,vo);

               }
           });

           rejectBtn.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick( View v )
               {
                   callAprroveAPI(false,vo);
               }
           });

       }
    }
}
