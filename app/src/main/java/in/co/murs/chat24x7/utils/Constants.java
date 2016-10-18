package in.co.murs.chat24x7.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Ujjwal on 6/16/2016.
 */
public class Constants {

    public static final String PROFILE_ID = "profile_id";

    public static final String ACTION_REGISTER = "in.co.murs.chat24x7.REGISTER";
    public static final String EXTRA_STATUS = "status";
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILED = 0;

    /**
     * Default lifespan (7 days) of a reservation until it is considered expired.
     */
    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;

    //parameters recognized by server
    public static final String FROM = "email";
    public static final String REG_ID = "regId";
    public static final String MSG = "msg";
    public static final String TO = "receiver";
    public static final String ONLINE = "online";
    public static final String RULY_ID = "rulyId";
    public static final String NAME = "name";


    public static final String LAWYER = "lawyerId";
    public static final String USER = "userId";
    public static final String USER_PIC = "userPic";
    public static final String LAWYER_PIC = "lawyerPic";
    public static final String USER_NAME = "userName";
    public static final String LAWYER_NAME = "lawyerName";
    public static final String CONSULT_KEY = "consultKey";
    public static final String QUERY = "query";
    public static final String STATUS = "status";
    public static final String CREATED = "created";

    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String CREATED_TIME = "createdTime";
    public static final String DELIVERED_TIME = "deliveredTime";
    public static final String MESSAGE = "message";
    public static final String RECEIVED_TIME = "receivedTime";


    //Request Tags
    public static final String VOLLEY = "Volley Request";

    //DEBUG LEVEL
    public static final Boolean DEBUG = true;

    /** The Constant EXTRA_DATA. */
    public static final String EXTRA_DATA = "extraData";

    /** Object Mapper Jackson Library*/
    public static final ObjectMapper mapper = new ObjectMapper();



}
