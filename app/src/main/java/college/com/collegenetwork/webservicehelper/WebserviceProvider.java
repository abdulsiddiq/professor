package college.com.collegenetwork.webservicehelper;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Krypto on 20-06-2017.
 */

public class WebserviceProvider extends AsyncTask<Void, Void, Object>
{

    IWebResponseProcessor _processor;
    private JSONObject _jsonBody;
    int CONNECTION_TIMEOUT = 3*1000*60;
    int DATARETRIEVAL_TIMEOUT = 3*1000*60;
    String _url;
    RequestType _requestType;
    int statusCode;

    public enum RequestType
    {
        GET, POST
    }


    @Override
    protected Object doInBackground( Void... params )
    {
        HttpURLConnection urlConnection = null;
        int statusCode = 0;
        Object responseObj = null;
        try {

            // create connection
            URL urlToRequest = new URL(_url);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
            urlConnection.setUseCaches (false);
            urlConnection.setRequestProperty("Content-Type","application/json");


            if(_jsonBody != null)
            {
                //setup send
                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(_jsonBody.toString().getBytes());
                //clean up
                os.flush();
                os.close();
            }

            // handle issues
            statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }


            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            responseObj = new JSONObject(getResponseText(in));
            in.close();
        } catch (MalformedURLException e) {
            // URL is invalid
            responseObj = e;
            statusCode = 111;
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            responseObj = e;
            statusCode = 222;
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            responseObj = e;
            statusCode = 333;
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        this.statusCode = statusCode;
        return responseObj;
    }

    @Override
    protected void onPostExecute( Object responseObj)
    {
        super.onPostExecute(responseObj);
        if(_processor != null)
        {
            _processor.processResponse(responseObj, this.statusCode);
        }
    }

    private String getResponseText( InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    public WebserviceProvider(String url,RequestType type,IWebResponseProcessor processor)
    {
        this(url,type,processor,null);
    }

    public WebserviceProvider(String url,RequestType type,IWebResponseProcessor processor,JSONObject body)
    {
        _url = url;
        _processor = processor;
        _jsonBody = body;
        _requestType = type;
    }
}
