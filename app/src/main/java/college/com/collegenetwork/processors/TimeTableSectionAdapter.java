package college.com.collegenetwork.processors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.MyTextView;

/**
 * Created by Krypto on 02-12-2017.
 */

public class TimeTableSectionAdapter extends RecyclerView.Adapter<TimeTableSectionAdapter.ItemHolder>
{

    private ArrayList<SubjectVO> subjectVOS;

    public TimeTableSectionAdapter(ArrayList<SubjectVO> subjectVOS)
    {
        this.subjectVOS = subjectVOS;
    }


    @Override
    public ItemHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_item,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position )
    {
        holder.onBind(subjectVOS.get(position));
    }

    @Override
    public int getItemCount()
    {
        return subjectVOS.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder
    {
        MyTextView venue;
        MyTextView subName;
        MyTextView subStream;
        public ItemHolder( View itemView )
        {
            super(itemView);
            venue = (MyTextView) itemView.findViewById(R.id.venue);
            subName = (MyTextView) itemView.findViewById(R.id.subName);
            subStream = (MyTextView) itemView.findViewById(R.id.subStream);
        }

        public void onBind(SubjectVO subjectVO)
        {
            venue.setText(subjectVO.getTiming());
            subName.setText(subjectVO.getSubName());
            subStream.setText(subjectVO.getStream());
        }
    }
}
