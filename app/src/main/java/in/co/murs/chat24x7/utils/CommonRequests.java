package in.co.murs.chat24x7.utils;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import in.co.murs.chat24x7.MursApplication;

/**
 * Created by Ujjwal on 6/18/2016.
 */
public class CommonRequests {

    /**
     * register is to register the user on app engine
     * @param url
     * @param listener
     */
    public static void register(String url, Response.Listener<String> listener){
        Log.d(Constants.VOLLEY, "Register");

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                "",
                listener,
                errorListener
        );
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        MursApplication.getInstance().getRequestQueue().add(request);
    }

    /**
     * register is to register the user on app engine
     * @param url
     * @param listener
     */
    public static void onlineUsers(String url, Response.Listener<String> listener){
        Log.d(Constants.VOLLEY, "Online Users");

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                "",
                listener,
                errorListener
        );
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        MursApplication.getInstance().getRequestQueue().add(request);
    }

    /**
     * get request
     * @param url
     * @param listener
     */
    public static void getServerRequest(String message, String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        Log.d(Constants.VOLLEY, message);

        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                "",
                listener,
                errorListener
        );
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        MursApplication.getInstance().getRequestQueue().add(request);
    }

    /**
     * post request
     * @param url
     * @param listener
     */
    public static void postServerRequest(String message, String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        Log.d(Constants.VOLLEY, message);

        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                "",
                listener,
                errorListener
        );
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        MursApplication.getInstance().getRequestQueue().add(request);
    }


}
