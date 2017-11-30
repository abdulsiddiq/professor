package college.com.collegenetwork.models;

import college.com.collegenetwork.utilpacks.CriticalConstants;

/**
 * Created by Krypto on 19-08-2017.
 */

public class SubjectVO
{
    private String subName;
    private String prof;
    private String timing;
    private String stream;
    private int enrollState = CriticalConstants.UN_ENROLLED;
    private int _totalCap;

    public SubjectVO(String subName , String prof, String timing)
    {
        this.subName = subName;
        this.prof = prof;
        this.timing = timing;
    }

    public String getTiming()
    {
        return this.timing;
    }

    public String getSubName()
    {
        return this.subName;
    }

    public String getStream()
    {
        return this.stream;
    }

    public void setStream(String stream)
    {
        this.stream= stream;
    }

    public String getProf()
    {
        return this.prof;
    }

    public int getEnrollState()
    {
        return this.enrollState;
    }

    public void setEnrollState(int state)
    {
        this.enrollState = state;
    }

    public void setCap(int cap)
    {
        _totalCap = cap;
    }

    public int getCap()
    {
        return _totalCap;
    }
    @Override
    public boolean equals( Object obj )
    {
        if(obj instanceof SubjectVO)
        {
            SubjectVO vo = (SubjectVO) obj;
            if(vo.getSubName().equals(this.getSubName()))
            {
                if(vo.getProf().equals(this.getProf()))
                {
                    return true;
                }
            }
            return false;
        }
        return super.equals(obj);
    }

    public boolean isEnrolled()
    {
        return this.enrollState != CriticalConstants.UN_ENROLLED;
    }
}
