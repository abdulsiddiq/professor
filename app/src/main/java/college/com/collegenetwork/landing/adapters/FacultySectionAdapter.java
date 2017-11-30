package college.com.collegenetwork.landing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.SubjectVO;

/**
 * Created by Krypto on 28-08-2017.
 */

public class FacultySectionAdapter extends BaseAdapter
{
    ArrayList<SubjectVO> _subjects;
    Context _context;

    public FacultySectionAdapter(Context context, ArrayList<SubjectVO> enrolledSubjects)
    {
        _subjects = new ArrayList<>();
        _subjects = enrolledSubjects;
        _context = context;
    }

    @Override
    public int getCount() {
        return _subjects.size();
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
            convertView = LayoutInflater.from(_context).inflate(R.layout.student_section_item,null);
            holder.studentName = (TextView) convertView.findViewById(R.id.sub_name);
            holder.subject = (TextView) convertView.findViewById(R.id.prof_name);
            holder.venue= (TextView) convertView.findViewById(R.id.venue);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemHolder) convertView.getTag();
        }
        SubjectVO vo = getItem(position);
        holder.studentName.setText(vo.getProf());
        holder.subject.setText(vo.getSubName());
        holder.venue.setText(vo.getTiming());

        return convertView;
    }

    private class ItemHolder
    {
        TextView studentName;
        TextView subject;
        TextView venue;

    }
}
