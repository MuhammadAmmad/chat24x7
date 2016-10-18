package in.co.murs.chat24x7.models;

import java.io.Serializable;

/**
 * Created by Ujjwal on 6/16/2016.
 */
public class Credentials implements Serializable {
    private String name;
    private String email;
    private int id;
    private boolean online;

    public Credentials(){}

    public Credentials(String name, String email, int id){
        this.name = name;
        this.email = email;
        this.id = id;
        this.online = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline(){
        return online;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
