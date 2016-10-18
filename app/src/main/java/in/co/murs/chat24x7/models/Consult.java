package in.co.murs.chat24x7.models;

/**
 * Created by Ujjwal on 6/16/2016.
 */

import java.io.Serializable;

/**
 * @author appsrox.com
 *
 */
public class Consult implements Serializable{

    private Long id;
    private String lawyerId;
    private String userId;
    private String lawyerName;
    private String userName;
    private String userPic;
    private String lawyerPic;
    private String query;
    private int status;
    private String consultKey;
    private String created;  //yyyy-mm-dd hh:mm

    public Consult() {}

    public Consult(String lawyerId, String userId, String userPic, String lawyerPic,
                   String lawyerName, String userName, String query, int status, String consultKey, String created) {
        this.lawyerId = lawyerId;
        this.userId = userId;
        this.userPic = userPic;
        this.lawyerPic = lawyerPic;
        this.query = query;
        this.status = status;
        this.consultKey = consultKey;
        this.created = created;
        this.userName = userName;
        this.lawyerName = lawyerName;
    }

    public Long getId() {
        return id;
    }

    public String getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserName(String userName) {
        this.userName= userName;
    }

    public String getLawyerPic() {
        return lawyerPic;
    }

    public void setLawyerPic(String lawyerPic) {
        this.lawyerPic = lawyerPic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getConsultKey() {
        return consultKey;
    }

    public void setConsultKey(String consultKey) {
        this.consultKey = consultKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public String getUserName() {
        return userName;
    }

}

