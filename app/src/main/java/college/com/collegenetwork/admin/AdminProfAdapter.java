package college.com.collegenetwork.admin;

import android.content.Context;
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
import college.com.collegenetwork.models.ProfessorVO;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.MyTextView;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-11-2017.
 */

public class AdminProfAdapter extends RecyclerView.Adapter<AdminProfAdapter.ItemHolder>
{
    ArrayList<ProfessorVO> _professors;
    Context _context;

    public AdminProfAdapter(Context context, ArrayList<ProfessorVO> professors)
    {
        _professors = new ArrayList<>();
        _professors = professors;
        _context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        View convertView = LayoutInflater.from(_context).inflate(R.layout.admin_subject_item,null);
        return new ItemHolder(convertView);
    }

    @Override
    public void onBindViewHolder( ItemHolder holder, int position )
    {
        holder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        return _professors.size()+1;
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

    class ItemHolder extends RecyclerView.ViewHolder
    {
        MyTextView _subTV;


        public ItemHolder( View itemView )
        {
            super(itemView);
            _subTV = (MyTextView) itemView.findViewById(R.id.subject);
        }

        public void bind(int position)
        {
            if(position == 0)
            {
                _subTV.setText("Add Professor");
                _subTV.setOnClickListener(new AddProfHandler());
            }
            else
            {
                ProfessorVO vo = _professors.get(position-1);
                _subTV.setText(vo.getName());
                _subTV.setOnClickListener(new EditProfHandler(vo));
            }


        }
    }
}
