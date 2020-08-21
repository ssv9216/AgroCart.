package ssv.com.agrocart;

import com.google.firebase.database.Exclude;

public class GlobalChat {
    String Message;
    String UserId;
    String time;
    String key;
    String date;


    public GlobalChat(String message, String userId, String time, String date) {
        Message = message;
        UserId = userId;
        this.time = time;
        this.date = date;

    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
