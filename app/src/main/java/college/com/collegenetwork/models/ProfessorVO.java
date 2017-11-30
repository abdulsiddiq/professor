package college.com.collegenetwork.models;

/**
 * Created by Krypto on 03-11-2017.
 */

public class ProfessorVO
{
    String name;
    String email;
    String userId;
    String stream;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    public String getStream()
    {
        return stream;
    }

    public void setStream( String stream )
    {
        this.stream = stream;
    }
}
