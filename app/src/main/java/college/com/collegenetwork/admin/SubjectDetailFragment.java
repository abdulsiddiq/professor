package college.com.collegenetwork.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import college.com.collegenetwork.R;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.processors.SubjectDetailAdapter;
import college.com.collegenetwork.utilpacks.BaseFragment;

/**
 * Created by Krypto on 02-12-2017.
 */

public class SubjectDetailFragment extends BaseFragment
{

    RecyclerView lists;
    ArrayList<SubjectVO> _vos;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.approval_screen,container,false);
    }


    public void setSubject(ArrayList<SubjectVO> vos)
    {
        _vos = vos;
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);
        lists = (RecyclerView) view.findViewById(R.id.approval_list);

        lists.setLayoutManager(new LinearLayoutManager(getContext()));

        SubjectDetailAdapter adapter = new SubjectDetailAdapter(_vos);
        lists.setAdapter(adapter);


    }
}
