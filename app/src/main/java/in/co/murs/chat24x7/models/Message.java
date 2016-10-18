package in.co.murs.chat24x7.models;

import java.io.Serializable;

/**
 * Created by Ujjwal on 6/16/2016.
 */

public class Message implements Serializable, Comparable<Message>{

    private Long id;
    private String sender;
    private String receiver;
    private String message;
    private Long createdTime;
    private Long receivedTime;
    private String consultKey;
    private boolean delivered;

    public Message() {
    }

    public Message(String sender, String receiver, String message, Long createdTime,  Long receivedTime,
                   boolean delivered) {
        this.setSender(sender);
        this.setReceiver(receiver);
        this.setMessage(message);
        this.createdTime = createdTime;
        this.setReceivedTime(receivedTime);
        this.setDeliveredTime(delivered);
    }

    public Message(String sender, String receiver, String message, Long createdTime, String consultKey) {
        this.setSender(sender);
        this.setReceiver(receiver);
        this.setMessage(message);
        this.consultKey = consultKey;
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDeliveredTime(boolean delivered) {
        this.delivered = delivered;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public int compareTo(Message another) {
        if (this.getReceivedTime() < another.getReceivedTime()){
            return -1;
        } else{
            return 1;
        }
    }

    public String getConsultKey() {
        return consultKey;
    }

    public void setConsultKey(String consultKey) {
        this.consultKey = consultKey;
    }
}

