package in.co.murs.chat24x7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import in.co.murs.chat24x7.MursApplication;
import in.co.murs.chat24x7.R;
import in.co.murs.chat24x7.utils.CommonRequests;
import in.co.murs.chat24x7.utils.Constants;
import in.co.murs.chat24x7.utils.GcmUtil;

/**
 * The Class Login is an Activity class that shows the login screen to users.
 * The current implementation simply includes the options for Login and button
 * for Register. On login button click, it sends the Login details to Parse
 * server to verify user.
 */
public class MainActivity extends BaseActivity {

    @InjectView(R.id.etUserName)
    EditText etUsername;

    @InjectView(R.id.etEmail)
    EditText etEmail;

    @InjectView(R.id.tvResponse)
    TextView tvResponse;

    private static GcmUtil gcmUtil;

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MursApplication.setUserCredentials("ujjwal singh", "ujjwal_rocks@rediffmail.com", 10);

        ButterKnife.inject(this);
        gcmUtil = new GcmUtil(this);
    }

    @OnClick(R.id.btnReg)
    public void onClickRegister(View view) {
        //todo register user
        boolean check = true;
        if (TextUtils.isEmpty(etUsername.getText()))
            check = false;

        if (TextUtils.isEmpty(etEmail.getText()))
            check = false;

        if (check) {

            String url = MursApplication.getServerUrl() + "/register";

            String params = Constants.FROM + "=" + etEmail.getText()
                    + "&" + Constants.REG_ID + "=" + MursApplication.getRegId()
                    + "&" + Constants.ONLINE + "=" + true
                    + "&" + Constants.NAME + "=" + etUsername.getText()
                    + "&" + Constants.RULY_ID + "=" + 1;
            Response.Listener<String> listener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    tvResponse.setText(response);
                }
            };
            CommonRequests.register(url + "?" + params, listener);
        }
    }

    @OnClick(R.id.btnUser)
    public void onClickUser(View view) {
        //todo register user

        String url = MursApplication.getServerUrl() + "/register";
        String params = Constants.SENDER + "=" + MursApplication.getPreferredEmail();
        Response.Listener<String> listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                tvResponse.setText(response);
            }
        };
        CommonRequests.onlineUsers(url + "?" + params, listener);
    }

    @OnClick(R.id.btnChat)
    public void onClickChat(View view){
        if(Constants.DEBUG)
            System.out.println("OnClickChat");
        Intent intent = new Intent(this, RoomListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
