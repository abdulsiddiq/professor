package college.com.collegenetwork.landing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class ApprovalAdapter  extends BaseAdapter
{

    ArrayList<ApprovalVO> _approvalVOs;
    Context _context;
    public ApprovalAdapter( Context context , ArrayList<ApprovalVO> approvalVOs )
    {
        _context = context;
        _approvalVOs = approvalVOs;
    }

    @Override
    public int getCount()
    {
        return _approvalVOs.size();
    }

    @Override
    public ApprovalVO getItem( int position )
    {
        return _approvalVOs.get(position);
    }

    @Override
    public long getItemId( int position )
    {
        return 0;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        ItemHolder holder;
        if(convertView == null)
        {
            holder = new ItemHolder();
            convertView = LayoutInflater.from(_context).inflate(R.layout.student_section_item,null);
            holder.studentName = (TextView) convertView.findViewById(R.id.sub_name);
            holder.subjectName = (TextView) convertView.findViewById(R.id.prof_name);
            holder.venue= (TextView) convertView.findViewById(R.id.venue);
            holder.approveBtn= (TextView) convertView.findViewById(R.id.approveBtn);
            holder.rejectBtn= (TextView) convertView.findViewById(R.id.rejectBtn);
            holder.approveBtn.setVisibility(View.VISIBLE);
            holder.rejectBtn.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemHolder) convertView.getTag();
        }
        final ApprovalVO vo = getItem(position);
        holder.subjectName.setText(vo.getSubject());
        holder.studentName.setText(vo.getStudentName());
        holder.venue.setText(vo.getTiming());

        holder.approveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {

                callAprroveAPI(true,vo);

            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                callAprroveAPI(false,vo);
            }
        });

        return convertView;

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

    private class ItemHolder
    {
        TextView subjectName;
        TextView studentName;
        TextView venue;
        TextView approveBtn;
        TextView rejectBtn;

    }
}
