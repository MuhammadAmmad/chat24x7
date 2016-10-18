package in.co.murs.chat24x7;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Patterns;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import in.co.murs.chat24x7.models.Credentials;
import in.co.murs.chat24x7.utils.DbHelper;

/**
 * Created by Ujjwal on 6/16/2016.
 */

public class MursApplication extends Application {

    private static SharedPreferences prefs;
    private static Credentials userCredentials;
    private static RequestQueue mRequestQueue;
    private static String mRegId;
    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
    private static String SERVER_URL = "http://1-dot-onlinecomm-1283.appspot.com";

    /**
     * Google API project id registered to use GCM.
     */
    private static String SENDER_ID = "623759057842";

    public static MursApplication sInstance;

    public static DbHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        db = new DbHelper(this);
        mRequestQueue = Volley.newRequestQueue(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private List<String> getEmailList() {
        List<String> lst = new ArrayList<String>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                lst.add(account.name);
            }
        }
        return lst;
    }

    public static String getPreferredEmail() {
        return userCredentials.getEmail();
    }

    public static String getDisplayName() {
        String email = getPreferredEmail();
        return prefs.getString("display_name", userCredentials.getName());
    }

    public static boolean isNotify() {
        return prefs.getBoolean("notifications_new_message", true);
    }

    public static String getRingtone() {
        return prefs.getString("notifications_new_message_ringtone", android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    }

    public static String getServerUrl() {
        return prefs.getString("server_url_pref", SERVER_URL);
    }

    public static String getSenderId() {
        return prefs.getString("sender_id_pref", SENDER_ID);
    }

    public static void setUserCredentials(String name, String email, int id){
        userCredentials = new Credentials(name, email, id);
    }

    public static void setUserRegId(String regId){
        mRegId = regId;
    }

    public static String getRegId(){
        return mRegId;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public synchronized static MursApplication getInstance() {
        return sInstance;
    }
}
