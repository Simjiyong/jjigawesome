package kr.co.company.jjigawesome;

/**
 * Created by BHY on 2018. 8. 17..
 */

public class Coupon {
    private String location;
    private String createDate;
    private int type;
    private int remarks;
    private int totalnumber;
    private String stampname;
    private String key;
    private int stamp_number;
    private int thema;
    private String couponname;
    private Row data;
    private String logo;
    private int num;

    public Coupon(){}

    public Coupon(int type){
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public String getCreateDate() {
        return createDate;
    }

    public int getType() {
        try {
            if(stampname !=null) {
                type = Integer.parseInt(stampname);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return type;
    }

    public int getRemarks() {
        return remarks;
    }

    public int getTotalnumber() {
        return totalnumber;
    }

    public String getStampname() {
        return stampname;
    }

    public String getKey() {
        return key;
    }

    public int getStamp_number() {
        return stamp_number;
    }

    public int getThema() {
        return thema;
    }

    public String getCouponname() {
        return couponname;
    }

    public Row getData() {
        return data;
    }

    public void setData(Row data) {
        this.data = data;
    }

    public String getLogo() {
        return logo;
    }

    public int getNum() {
        return num;
    }
}
