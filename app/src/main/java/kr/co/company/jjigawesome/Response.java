package kr.co.company.jjigawesome;

/**
 * Created by BHY on 2018. 7. 20..
 */

public class Response {
    private String status;
    private String token;
    private String name;
    private int stamp_number;
    private int type;
    private String error;

    public Response(){}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStamp_number() {
        return stamp_number;
    }

    public void setStamp_number(int stamp_number) {
        this.stamp_number = stamp_number;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
