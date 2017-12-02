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
import college.com.collegenetwork.utilpacks.ApplicationUrl;
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
        View convertView = LayoutInflater.from(_context).inflate(R.layout.adminprofitem,null);
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
        TextView profName;
        TextView profEmail;
        TextView profStream;
        TextView addBtn;
        TextView editBtn;
        TextView deleteBtn;
        LinearLayout detailRow;

        public ItemHolder( View itemView )
        {
            super(itemView);
            profName= (TextView) itemView.findViewById(R.id.profName);
            profStream = (TextView) itemView.findViewById(R.id.profStream);
            profEmail= (TextView) itemView.findViewById(R.id.profMail);
            addBtn= (TextView) itemView.findViewById(R.id.newBtn);
            editBtn= (TextView) itemView.findViewById(R.id.editBtn);
            detailRow= (LinearLayout) itemView.findViewById(R.id.rowData);
            deleteBtn = (TextView) itemView.findViewById(R.id.deleteBtn);
        }

        public void bind(int position)
        {
            if (position == 0)
            {
                detailRow.setVisibility(View.GONE);
                addBtn.setVisibility(View.VISIBLE);

                addBtn.setOnClickListener(new AddProfHandler());

            }
            else
            {
                final ProfessorVO vo = _professors.get(position-1);
                detailRow.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.GONE);


                profName.setText(vo.getName());
                profEmail.setText(vo.getEmail());
                profStream.setText(vo.getStream());

                editBtn.setOnClickListener(new EditProfHandler(vo));

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


        }
    }
}
