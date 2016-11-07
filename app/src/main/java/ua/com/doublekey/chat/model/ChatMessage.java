package ua.com.doublekey.chat.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by doublekey on 03.10.2016.
 *
 * The message pojo
 */

@Entity
public class ChatMessage {
    @Id(autoincrement = true)
    private Long id;

    private String sender;
    private String receiver;
    private String message;
    private long dateTime;
    private boolean isMine;

    public ChatMessage(String sender, String receiver, String message, boolean isMine) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isMine = isMine;
        this.dateTime = System.currentTimeMillis();
    }


    @Generated(hash = 1453586703)
    public ChatMessage(Long id, String sender, String receiver, String message, long dateTime,
            boolean isMine) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.dateTime = dateTime;
        this.isMine = isMine;
    }


    @Generated(hash = 2271208)
    public ChatMessage() {
    }


    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public long getDateTime() {
        return dateTime;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean getIsMine() {
        return this.isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
