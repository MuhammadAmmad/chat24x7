package in.co.murs.chat24x7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.co.murs.chat24x7.MursApplication;
import in.co.murs.chat24x7.R;
import in.co.murs.chat24x7.models.Consult;
import in.co.murs.chat24x7.models.Credentials;
import in.co.murs.chat24x7.utils.CommonRequests;
import in.co.murs.chat24x7.utils.Constants;

/**
 * The Class UserList is the Activity class. It shows a list of all users of
 * this app. It also shows the Offline/Online status of users.
 */
public class RoomListActivity extends BaseActivity {

    /**
     * The Chat list.
     */
    private ArrayList<Credentials> uList;

    @InjectView(R.id.llRoomList)
    ListView llOnlineUsers;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        ButterKnife.inject(this);
        //getActionBar().setDisplayHomeAsUpEnabled(false);
        updateUserStatus(true);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        getUserList();

    }

    /**
     * Update user status.
     *
     * @param status true if user is online
     */
    private void updateUserStatus(boolean status) {
        String url = MursApplication.getServerUrl() + "/online";

        String params = Constants.FROM + "=" + MursApplication.getPreferredEmail()
                + "&" + Constants.REG_ID + "=" + MursApplication.getRegId()
                + "&" + Constants.ONLINE + "=" + status;

        Response.Listener<String> listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //todo check if response is true
            }
        };
        CommonRequests.register(url + "?" + params, listener);

    }

    private void getUserList() {
        //todo register user

        String url = MursApplication.getServerUrl() + "/register";
        String params = Constants.SENDER + "=" + MursApplication.getPreferredEmail();
        Response.Listener<String> listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(Constants.DEBUG)
                    System.out.println(response);
                try{
                    JSONArray contacts = new JSONArray(response);
                    uList = new ArrayList<Credentials>();
                    for (int i = 0 ; i < contacts.length(); i++){
                        JSONObject details = contacts.getJSONObject(i);
                        Credentials contact = new Credentials(details.getString("name"),
                                details.getString("email"),
                                details.getInt("rulyId"));
                        uList.add(contact);
                    }
                    if(uList.size() > 0){

                        llOnlineUsers.setAdapter(new UserAdapter());
                        llOnlineUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int pos, long arg3)
                            {
                                Intent i = new Intent(RoomListActivity.this, ChatActivity.class);
                                Consult consult = new Consult("ujjwal_rocks@rediffmail.com", "ntaiit@yahoo.com",
                                        "https://graph.facebook.com/900224946693780/picture?type=large",
                                        "https://media.licdn.com/mpr/mprx/0_bvDvUeKPeX7XrEeu5KOQUItaHkHNKEduLNJoUoAmcTpk7aO2IPjMzE586oew1SH8QnfwNS7pBP0Z",
                                        "This is a test", 1,"TTYFF","2016-06-19 08:35:33");
                                try {
                                    i.putExtra(Constants.EXTRA_DATA, Constants.mapper.writeValueAsString(consult));
                                    startActivity(i);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }else{
                        //todo show no online user
                            Toast.makeText(RoomListActivity.this,
                                    R.string.msg_no_user_found,
                                    Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        };
        CommonRequests.onlineUsers(url + "?" + params, listener);
    }


    /**
     * The Class UserAdapter is the adapter class for User ListView. This
     * adapter shows the user name and it's only online status for each item.
     */
    private class UserAdapter extends BaseAdapter {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return uList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Credentials getItem(int arg0) {
            return uList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.item_chat, null);

            Credentials c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getName());
            lbl.setCompoundDrawablesWithIntrinsicBounds(
                    c.isOnline() ? R.drawable.ic_online
                            : R.drawable.ic_offline, 0, R.drawable.arrow, 0);

            return v;
        }
    }

}

