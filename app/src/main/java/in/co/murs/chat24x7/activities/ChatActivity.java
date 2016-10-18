package in.co.murs.chat24x7.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import in.co.murs.chat24x7.MursApplication;
import in.co.murs.chat24x7.R;
import in.co.murs.chat24x7.models.Consult;
import in.co.murs.chat24x7.models.Message;
import in.co.murs.chat24x7.utils.CommonRequests;
import in.co.murs.chat24x7.utils.Constants;
import in.co.murs.chat24x7.utils.DbHelper;

/**
 * The Class Chat is the Activity class that holds main chat screen. It shows
 * all the conversation messages between two users and also allows the user to
 * send and receive messages.
 */
public class ChatActivity extends BaseActivity {

    /**
     * The Conversation list.
     */
    private ArrayList<Message> convList;

    /**
     * The chat adapter.
     */
    private ChatAdapter adp;

    /**
     * The Editext to compose the message.
     */
    @InjectView(R.id.txt)
    EditText txt;

    /**
     * The user name of buddy.
     */
    private String buddy;

    /**
     * The date of last message in conversation.
     */
    private Date lastMsgDate;

    /**
     * Flag to hold if the activity is running or not.
     */
    private boolean isRunning;

    /**
     * The handler.
     */
    private static Handler handler;

    /**
     * Consult Object
     */
    private Consult consultation;

    /**
     * Time when lastMessage is received at the server
     */
    private Long lastMsgRcvTime = 0l;

    /**
     * Database Helper for Chat to store sent messages
     */
    private DbHelper dbHelper;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        convList = new ArrayList<Message>();
        ButterKnife.inject(this);

        ListView list = (ListView) findViewById(R.id.list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        txt = (EditText) findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        handler = new Handler();

        dbHelper = new DbHelper(this);

        try {
            consultation = Constants.mapper.readValue(getIntent().getStringExtra(Constants.EXTRA_DATA), Consult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            getActionBar().setTitle(consultation.getQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        loadConversationList();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }


    /**
     * Call this method to Send message to opponent. It does nothing if the text
     * is empty otherwise it creates a Parse object for Chat message and send it
     * to Parse server.
     */
    @OnClick(R.id.btnSend)
    public void sendMessage(View view) {
        if (txt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s = txt.getText().toString();
        String receiver = "";
        if (consultation.getLawyerId().equals(MursApplication.getPreferredEmail()))
            receiver = consultation.getUserId();
        else
            receiver = consultation.getLawyerId();

        final Message c = new Message(MursApplication.getPreferredEmail(), receiver, s, (new Date()).getTime(), consultation.getConsultKey());

        dbHelper.addMessage(c);
        //todo create local copy

        convList.add(c);
        adp.notifyDataSetChanged();
        txt.setText(null);

        //Post Request
        String url = MursApplication.getServerUrl() + "/sendmessage";
        String params = Constants.SENDER + "=" + c.getSender()
                + "&" + Constants.CONSULT_KEY + "=" + c.getConsultKey()
                + "&" + Constants.RECEIVER + "=" + c.getReceiver()
                + "&" + Constants.MESSAGE + "=" + c.getMessage()
                + "&" + Constants.CREATED_TIME + "=" + c.getCreatedTime();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Long time = Long.parseLong(response);
                    c.setReceivedTime(time);
                    updateMessageStatus(c);
                } catch (Exception e) {
                    e.printStackTrace();
                    c.setReceivedTime(-1l);
                    updateMessageStatus(c);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                c.setReceivedTime(-1l);
                updateMessageStatus(c);
            }
        };

        CommonRequests.postServerRequest("post messages: " + s,
                url + "?" + params, listener, errorListener);
    }

    /**
     *
     */
    private void updateMessageStatus(Message message) {
        for (int i = 0; i < convList.size(); i++) {
            if (convList.get(i).getCreatedTime() == message.getCreatedTime()
                    && convList.get(i).getConsultKey().equals(message.getConsultKey())
                    && convList.get(i).getSender().equals(message.getSender())) {
                convList.get(i).setReceivedTime(message.getReceivedTime());
                adp.notifyDataSetChanged();
                try {
                    dbHelper.deleteMessage(message.getConsultKey(), message.getReceivedTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Load the conversation list from Parse server and save the date of last
     * message that will be used to load only recent new messages
     */
    private void loadConversationList() {
        String url = MursApplication.getServerUrl() + "/sendmessage";
        String params = Constants.SENDER + "=" + MursApplication.getPreferredEmail()
                + "&" + Constants.CONSULT_KEY + "=" + consultation.getConsultKey()
                + "&" + Constants.RECEIVED_TIME + "=" + lastMsgRcvTime;

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (Constants.DEBUG) {
                    System.out.println(response);
                }
                if(response != null && !TextUtils.isEmpty(response)) {
                    try {

                        JSONArray msg = new JSONArray(response);
                        for (int i = 0; i < msg.length(); i++) {
                            JSONObject data = msg.getJSONObject(i);
                            Message message = Constants.mapper.readValue(data.toString(), Message.class);
                            if (message.getReceivedTime() > lastMsgRcvTime)
                                lastMsgRcvTime = message.getReceivedTime();
                            convList.add(message);
                        }
                        if (convList.size() > 0) {
                            adp.notifyDataSetChanged();
                        } else
                            lastMsgRcvTime = 0l;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (isRunning)
                            loadConversationList();
                    }
                }, 1500);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning)
                            loadConversationList();
                    }
                }, 1500);
            }
        };

        CommonRequests.getServerRequest("get messages for " + consultation.getConsultKey() + " > " + lastMsgRcvTime,
                url + "?" + params, listener, errorListener);
    }

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */
    private class ChatAdapter extends BaseAdapter {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return convList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Message getItem(int arg0) {
            return convList.get(arg0);
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
            Message c = getItem(pos);
            if (c.getSender().equals(MursApplication.getPreferredEmail()))
                v = getLayoutInflater().inflate(R.layout.item_chat_sent, null);
            else
                v = getLayoutInflater().inflate(R.layout.item_chat_rcv, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            try {
                if(c.getReceivedTime() > 0)
                    lbl.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this, c.getReceivedTime(), DateUtils.SECOND_IN_MILLIS,
                        DateUtils.DAY_IN_MILLIS, 0));
                else
                    lbl.setText("failed");
            } catch (Exception e) {
                lbl.setText("Sending...");
            }

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMessage());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isDelivered())
                lbl.setText("Delivered");
            else
                lbl.setText("");
            return v;
        }

    }
}

