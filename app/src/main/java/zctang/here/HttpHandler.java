package zctang.here;

/**
 * Created by wangxiaoyang01 on 2017/5/15.
 * Learn from blog http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi Tamada on 01/09/16.
 * www.androidhive.info
 */
public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();
    private String charset = "UTF-8";
    ;
    public HttpHandler() {
    }

    public String upvoteRequest(String reqUrl, String msgId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", requestType.UPVOTE.toString()));
        params.add(new BasicNameValuePair("msgid", msgId));

        return makeServiceCall(reqUrl, params);
    }

    public String TestRequest(String reqUrl) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", requestType.TEST.toString()));

        return makeServiceCall(reqUrl, params);
    }

    private String makeServiceCall(String reqUrl, List<NameValuePair> params) {
        String result = null;
        HttpPost httpPost = new HttpPost(reqUrl);

        try {
            HttpEntity httpEntity = new UrlEncodedFormEntity(params, charset);
            httpPost.setEntity(httpEntity);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(httpResponse.getEntity());
                Log.i(TAG, "Post result = " + result);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return result;
    }

    private enum requestType {TEST, UPVOTE}

}