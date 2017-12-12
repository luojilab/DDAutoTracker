package com.luojilab.bean;

/**
 * Created by liushuo on 2017/5/31.
 */

public class PointData {

    public static final String LOG_ID="log_id";
    public static final String LOG_TYPE="log_type";
    public static final String LOG_NAME="log_name";

    public static PointData create(String log_id, String log_type, String log_name) {
        return new PointData(log_id, log_type, log_name);

    }

    private String log_id;
    private String log_type;
    private String log_name;

    private PointData(String log_id, String log_type, String log_name) {
        this.log_id = log_id;
        this.log_type = log_type;
        this.log_name = log_name;
    }


    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getLog_type() {
        return log_type;
    }

    public void setLog_type(String log_type) {
        this.log_type = log_type;
    }

    public String getLog_name() {
        return log_name;
    }

    public void setLog_name(String log_name) {
        this.log_name = log_name;
    }
}
