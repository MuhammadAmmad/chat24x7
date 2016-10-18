package in.co.murs.chat24x7.utils;

/**
 * Created by Ujjwal on 6/16/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.co.murs.chat24x7.models.Message;


/**
 * Created by Ujjwal on 2/19/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ChatManager";

    // Table Names
    private static final String TABLE_SENT_MESSAGES= "sent_messages";

    // column names orders
    private static final String KEY_CONSULT = "consultKey";
    private static final String KEY_CREATED = "createdTime";
    private static final String KEY_MSG = "message";
    private static final String KEY_RECEIVER = "receiver";
    private static final String KEY_ID = "_id";


    // Table Create Statements
    private static final String CREATE_TABLE_SENT_MESSAGES = "CREATE TABLE "
            + TABLE_SENT_MESSAGES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_CONSULT + " TEXT ," + KEY_MSG
            + " TEXT," + KEY_CREATED + " INTEGER," + KEY_RECEIVER + " TEXT )";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SENT_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENT_MESSAGES);

        // create new tables
        onCreate(db);
    }

    /*****************************
     * Insert Table
     *************************************/

    public long addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECEIVER, message.getReceiver());
        values.put(KEY_MSG, message.getMessage());
        values.put(KEY_CREATED, message.getCreatedTime());
        values.put(KEY_CONSULT, message.getConsultKey());

        // insert row
        long _id = db.insert(TABLE_SENT_MESSAGES, null, values);
        return _id;
    }

    public boolean isMessageExist(String consultKey, long createdTime){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SENT_MESSAGES + " WHERE "
                + KEY_CONSULT + " = " + consultKey + " AND " + KEY_CREATED + " = " + createdTime;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            if(c.getPosition() == -1)
                return false;
            else return true;
        }
        return false;
    }

    public void deleteMessage(String consultKey, Long createdTime)throws Exception{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SENT_MESSAGES, KEY_CONSULT + " = ? AND " + KEY_CREATED + " = ?" ,
                new String[]{consultKey, ""+createdTime});
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}


