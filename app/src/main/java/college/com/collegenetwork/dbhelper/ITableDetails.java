package college.com.collegenetwork.dbhelper;

/**
 * Created by sushantalone on 08/06/17.
 */

public interface ITableDetails {

    int getTableVersion();
    String getCreateQuery();
    String getUpdateQuery();
}
