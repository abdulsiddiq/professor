package college.com.collegenetwork.ftre;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import college.com.collegenetwork.R;
import college.com.collegenetwork.landing.BaseLandingActivity;
import college.com.collegenetwork.models.SubjectVO;
import college.com.collegenetwork.utilpacks.ApplicationUrl;
import college.com.collegenetwork.utilpacks.BaseFragment;
import college.com.collegenetwork.utilpacks.CriticalConstants;
import college.com.collegenetwork.utilpacks.MySharedPref;
import college.com.collegenetwork.utilpacks.Utils;
import college.com.collegenetwork.webservicehelper.IWebResponseProcessor;
import college.com.collegenetwork.webservicehelper.WebserviceProvider;

/**
 * Created by Krypto on 03-07-2017.
 */

public class ChooseSubjectsFragment extends BaseFragment
{
    ArrayList<String> _subjectsOpted;
    LinearLayout _checkedTextViewGroup;
    String streamSelected;
    boolean _canCall = true;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.itemsselection, container, false);
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);
        _subjectsOpted = new ArrayList<>();
        Bundle bundle = getArguments();
        streamSelected = bundle.getString(FTLandingActivity.STREAM);
        _checkedTextViewGroup = (LinearLayout) view.findViewById(R.id.checkTextViewGroup);
        getActivity().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                if (_subjectsOpted.size() >= 3)
                {

                    JSONObject jsonObject = new JSONObject();
                    try
                    {
                        jsonObject.put("subjectOpted", _subjectsOpted.toString());
                        new MySharedPref(getContext()).subjectTourCompleted();
                    } catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                    startLandingActivity();
                } else
                {
                    Utils.showToast("Must Exactly select 3 subjects", getContext());
                }
            }
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Got It", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                fetchList();
            }
        });
        dialogBuilder.setMessage("Check and Uncheck the Box for enroll and drop");
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void startLandingActivity()
    {
        Intent intent = new Intent(getContext() , BaseLandingActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void populateSubjectList( final ArrayList<SubjectVO> map3 )
    {
        _checkedTextViewGroup.removeAllViews();

        for (final SubjectVO subjectVO : map3)
        {

            final CheckedTextView checkedTextView = new CheckedTextView(getContext());
            String title = "Name : " + subjectVO.getSubName();
            String subtitle = "Prof : " + subjectVO.getProf() + " Time : "+subjectVO.getTiming();
            checkedTextView.setText(title + "\n" + subtitle);
            checkedTextView.setBackground(getContext().getResources().getDrawable(R.drawable.rectangle));

            switch (subjectVO.getEnrollState())
            {
                case CriticalConstants.ENROLLED:
                    checkedTextView.setChecked(true);
                    break;
                case CriticalConstants.WAITING_APPROVAL:
                    checkedTextView.setChecked(false);
                    checkedTextView.setBackgroundColor(getContext().getResources().getColor(R.color.button_selectorcolor));
                    break;
                case CriticalConstants.UN_ENROLLED:
                    checkedTextView.setChecked(false);
                    break;
            }

            TypedValue value = new TypedValue();
            getActivity().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            checkedTextView.setCheckMarkDrawable(checkMarkDrawableResId);
            checkedTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    boolean isChecked = checkedTextView.isChecked();

                    if(!isChecked)
                    {
                        if(subjectVO.getEnrollState() == CriticalConstants.WAITING_APPROVAL)
                        {
                        }
                        else if(_subjectsOpted.size() < 3)
                        {
                            subjectVO.setEnrollState(CriticalConstants.ENROLLED);
                            preEnroll(subjectVO,checkedTextView);
                            enrollTheSubject(subjectVO, true);
                        }
                        else
                        {
                            askForProfPermission(subjectVO, checkedTextView);
                        }
                    }
                    else
                    {
                        askForConfirmation(subjectVO, checkedTextView);
                    }
                }
            });
            _checkedTextViewGroup.addView(checkedTextView);

        }
    }

    private void askForProfPermission( final SubjectVO subjectVO, final CheckedTextView checkedTextView)
    {
        _canCall = false;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                subjectVO.setEnrollState(CriticalConstants.WAITING_APPROVAL);
                preEnroll(subjectVO,checkedTextView);
                requestProfessor(subjectVO);
                checkedTextView.setBackgroundColor(getContext().getResources().getColor(R.color.white_greyish));
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
                _canCall = true;
                fetchList();
            }
        });
        dialogBuilder.setMessage("You can only enroll 3 subjects. You wanna send request to professor ?");
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

    }

    private void requestProfessor(SubjectVO subjectVO)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("timing", subjectVO.getTiming());
            jsonObject.put("prof", subjectVO.getProf());
            jsonObject.put("subject", subjectVO.getSubName());
            jsonObject.put("user",new MySharedPref(getContext()).getUserId());
            jsonObject.put("method", "send");
            jsonObject.put("stream", streamSelected);
        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        new WebserviceProvider(ApplicationUrl.REQUESTS, WebserviceProvider.RequestType.POST,new ApprovalResponseParser(),jsonObject).execute();

    }

    private void preEnroll(SubjectVO subjectVO, CheckedTextView checkedTextView)
    {
        checkedTextView.setChecked(true);
        _subjectsOpted.add(subjectVO.getSubName());

    }

    private void askForConfirmation(final SubjectVO subjectVO, final CheckedTextView checkedTextView)
    {
        _canCall = false;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                enrollTheSubject(subjectVO, false);
                subjectVO.setEnrollState(CriticalConstants.UN_ENROLLED);
                checkedTextView.setChecked(false);
                _subjectsOpted.remove(subjectVO.getSubName());
            }
        });
        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                checkedTextView.setChecked(true);
                dialog.dismiss();
            }
        });
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss( DialogInterface dialog )
            {
                _canCall = true;
                fetchList();
            }
        });
        dialogBuilder.setMessage("Are you sure want to drop the subject?");
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void enrollTheSubject( SubjectVO vo , boolean enroll)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("stream", streamSelected);
            jsonObject.put("subject", vo.getSubName());
            jsonObject.put("user",new MySharedPref(getContext()).getUserId());
            jsonObject.put("enroll",enroll);
            jsonObject.put("prof",vo.getProf());
            jsonObject.put("venue",vo.getTiming());
        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        new WebserviceProvider(ApplicationUrl.ENROLL, WebserviceProvider.RequestType.POST,new EnrollResponseParser(),jsonObject).execute();
    }


    private void fetchList()
    {
        if(getView() != null && _canCall)
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                MySharedPref pref = new MySharedPref(getContext());
                if(pref != null)
                {
                    String userId = pref.getUserId();
                    jsonObject.put("stream", streamSelected);
                    jsonObject.put("user",userId);
                    jsonObject.put("method","subjects");
                    new WebserviceProvider(ApplicationUrl.LISTFETCH, WebserviceProvider.RequestType.POST,new SubjectListResponse(),jsonObject).execute();
                }
            } catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }

    }

    ArrayList<SubjectVO> _subjects =  new ArrayList<>();
    private class SubjectListResponse implements IWebResponseProcessor
    {

        @Override
        public void processResponse( Object obj, int status )
        {
            if(_canCall)
            {
                JSONObject object = (JSONObject) obj;
//              updated code
                JSONArray ogarray = object.optJSONArray("subjectList");

                ArrayList<SubjectVO> updatedSubjectList = new ArrayList<>();

                for (int i = 0; i < ogarray.length(); i++)
                {
                    JSONObject subObj = ogarray.optJSONObject(i);
                    String subjectname = subObj.optString("name");
                    String subjectProf = subObj.optString("prof");
                    String subTiming = subObj.optString("timing");

                    SubjectVO subjectVO =  new SubjectVO(subjectname,subjectProf,subTiming);

                    updatedSubjectList.add(subjectVO);
                }


                Iterator<SubjectVO> iter = _subjects.iterator();

                while (iter.hasNext()) {
                    SubjectVO sub = iter.next();

                    if(!updatedSubjectList.contains(sub))
                    {
                        if(!sub.isEnrolled())
                        {
                            iter.remove();
                        }
                    }
                }

                for(int index = 0 ; index < updatedSubjectList.size(); index++)
                {
                    SubjectVO sub = updatedSubjectList.get(index);
                    if(!_subjects.contains(sub))
                    {
                        _subjects.add(sub);
                    }
                }


//                end code

                populateSubjectList(_subjects);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fetchList();
                    }
                }, 3 * 1000);
            }
        }
    }


    private class EnrollResponseParser implements IWebResponseProcessor
    {

        @Override
        public void processResponse( Object obj, int status )
        {
            JSONObject object = (JSONObject) obj;
            int responseCode = object.optInt("success");
            if(responseCode == 222)
            {
                Utils.showToast("Subject Capacity is reached",getContext());
            }
            else if (responseCode == 333)
            {
                Utils.showToast("Unexpected error",getContext());
            }

        }
    }


    private class ApprovalResponseParser implements IWebResponseProcessor
    {

        @Override
        public void processResponse( Object obj, int status )
        {

        }
    }
}
