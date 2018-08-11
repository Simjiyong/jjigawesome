package kr.co.company.jjigawesome;

/**
 * Created by BHY on 2018. 7. 20..
 */

public class Response {
    private String status;
    private String token;
    private String name;
    private int number;
    private int type;
    private String error;

    public Response(){}

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getType() {
        return type;
    }

    public String getError() {
        return error;
    }
}
