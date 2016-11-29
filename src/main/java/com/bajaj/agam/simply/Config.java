package com.bajaj.agam.simply;

/**
 * Created by Agam on 11/19/2016.
 */

public class Config {

    public static final String URL_ADD_USER="http://192.168.55.69/simply/addUser.php";
    public static final String URL_REGISTER_COMPLAINT="http://192.168.55.69/simply/addComplaint.php";

    public static final String URL_GET_ALL_USER_COMPLAINTS = "http://192.168.55.69/simply/getAlUserComplaints.php?u_email=";
    public static final String URL_GET_COMPLAINT = "http://192.168.55.69/simply/getComplaint.php?u_id=";
    public static final String URL_GET_CAR = "http://192.168.55.69/simply/getCar.php?plate_no=";
    public static final String URL_GET_USER = "http://192.168.55.69/simply/getUser.php?email=";

    public static final String URL_DELETE_COMPLAINT = "http://192.168.55.69/simply/delComplaint.php?id=u_id";

    //Keys that will be used to send the request to php scripts
//    public static final String KEY_EMP_ID = "id";
//    public static final String KEY_EMP_NAME = "name";
//    public static final String KEY_EMP_DESG = "desg";
//    public static final String KEY_EMP_SAL = "salary";

    //JSON Tags
//    public static final String TAG_JSON_ARRAY="result";
//    public static final String TAG_ID = "id";
//    public static final String TAG_NAME = "name";
//    public static final String TAG_DESG = "desg";
//    public static final String TAG_SAL = "salary";

    //employee id to pass with intent
//    public static final String EMP_ID = "emp_id";
}
