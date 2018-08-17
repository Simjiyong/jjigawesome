package kr.co.company.jjigawesome;

/**
 * Created by BHY on 2018. 8. 17..
 */

public class Coupon {
    private String location;
    private String date;
    private int type;
    private int remarks;
    private int totalnumber;

    public Coupon(){}

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public int getRemarks() {
        return remarks;
    }

    public int getTotalnumber() {
        return totalnumber;
    }
}
