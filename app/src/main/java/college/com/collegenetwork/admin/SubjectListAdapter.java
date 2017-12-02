package college.com.collegenetwork.admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.MyTextView;
import college.com.collegenetwork.utilpacks.SubjectClickListener;

/**
 * Created by Krypto on 01-12-2017.
 */

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder>
{

    ArrayList<SubjectVO> _subjects;
    SubjectClickListener _listener;

    public SubjectListAdapter( ArrayList<SubjectVO> subjects, SubjectClickListener listener)
    {
        _subjects = subjects;
        _listener = listener;
    }


    @Override
    public SubjectViewHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_subject_item,parent,false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder( SubjectViewHolder holder, int position )
    {
        holder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        return _subjects.size()+1;
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder
    {
        MyTextView _subTV;
        public SubjectViewHolder( View itemView )
        {
            super(itemView);
            _subTV = (MyTextView) itemView.findViewById(R.id.subject);
        }

        public void bind(int pos)
        {
            if(pos == 0)
            {
                _subTV.setText("Add Subject");
                _subTV.setOnClickListener(new NewSubjectHandler());
            }
            else
            {
                final SubjectVO vo = _subjects.get(pos-1);
                _subTV.setText(vo.getSubName());
                _subTV.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick( View v )
                    {
                        _listener.onClickSubject(vo);
                    }
                });
            }
        }
    }
}
