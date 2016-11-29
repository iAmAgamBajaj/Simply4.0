package com.bajaj.agam.simply;

public class ComplaintSchema {

    private String title;
    private String body;
    private String date;
    private int status;
    private String uID;
    private String eID;

    public ComplaintSchema(String title,String body, String date, String status, String uID, String eID)
    {
        this.setTitle(title);
        this.setBody(body);
        this.setDate(date);
        this.setuID(uID);
        this.seteID(eID);
        this.setStatus(Integer.valueOf(status));
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public String getuID() {
        return uID;
    }

    public String geteID() {
        return eID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void seteID(String eID) {
        this.eID = eID;
    }
}
