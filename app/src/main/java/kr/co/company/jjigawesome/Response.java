package kr.co.company.jjigawesome;

import java.util.ArrayList;
import java.util.LinkedList;

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
    private ArrayList<Coupon> stamp;
    private String logo;
    private ApiResponse SearchCulturalFacilitiesDetailService;

    public Response(){
        stamp = new ArrayList<>();
    }

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

    public ArrayList<Coupon> getStamp() {
        return stamp;
    }

    public ApiResponse getSearchCulturalFacilitiesDetailService() {
        return SearchCulturalFacilitiesDetailService;
    }

    public String getLogo() {
        return logo;
    }
}
