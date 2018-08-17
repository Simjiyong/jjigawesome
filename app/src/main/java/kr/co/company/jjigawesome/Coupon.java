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

}
