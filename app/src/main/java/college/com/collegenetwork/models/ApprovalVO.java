package college.com.collegenetwork.models;

/**
 * Created by Krypto on 28-08-2017.
 */

public class ApprovalVO
{
    String studentName;
    String timing;
    String subject;
    String stream;

    public ApprovalVO(String studentName, String timing,String subject,String stream)
    {
        this.studentName = studentName;
        this.timing = timing;
        this.subject = subject;
        this.stream = stream;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public String getTiming()
    {
        return timing;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getStream()
    {
        return stream;
    }
}
