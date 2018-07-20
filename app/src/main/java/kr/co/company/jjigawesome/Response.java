package kr.co.company.jjigawesome;

/**
 * Created by BHY on 2018. 7. 20..
 */

public class Response {
    private String status;
    private String token;
    private String name;
    private int stamp_number;

    public Response(){}

    public Response(String status, String token, String name, int stamp_number) {
        this.status = status;
        this.token = token;
        this.name = name;
        this.stamp_number = stamp_number;
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
}
