package kr.co.company.jjigawesome;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by BHY on 2018. 9. 5..
 */

public class ApiResponse {
    private Result RESULT;
    private ArrayList<Row> row;

    public ApiResponse(){
        row = new ArrayList<>();
    }

    public ArrayList<Row> getRow() {
        return row;
    }

    public Result getRESULT() {
        return RESULT;
    }
}

class Row implements Parcelable{
    private String FAC_CODE;
    private String SUBJCODE;
    private String CODENAME;
    private String FAC_NAME;
    private String MAIN_IMG;
    private String ADDR;
    private String PHNE;
    private String FAX;
    private String HOMEPAGE;
    private String OPENHOUR;
    private String ENTR_FEE;
    private String CLOSEDAY;
    private String OPEN_DAY;
    private String SEAT_CNT;
    private String X_COORD;
    private String Y_COORD;
    private String ETC_DESC;
    private String FAC_DESC;
    private String ENTRFREE;

    public Row(Parcel parcel) {
        this.MAIN_IMG = parcel.readString();
        this.FAC_NAME = parcel.readString();
        this.ADDR = parcel.readString();
        this.OPENHOUR = parcel.readString();
        this.CLOSEDAY = parcel.readString();
        this.HOMEPAGE = parcel.readString();
        this.PHNE = parcel.readString();
        this.FAC_DESC = parcel.readString();
    }

    public String getFAC_CODE() {
        return FAC_CODE;
    }

    public String getSUBJCODE() {
        return SUBJCODE;
    }

    public String getCODENAME() {
        return CODENAME;
    }

    public String getFAC_NAME() {
        return FAC_NAME;
    }

    public String getMAIN_IMG() {
        return MAIN_IMG;
    }

    public String getADDR() {
        return ADDR;
    }

    public String getPHNE() {
        return PHNE;
    }

    public String getFAX() {
        return FAX;
    }

    public String getHOMEPAGE() {
        return HOMEPAGE;
    }

    public String getOPENHOUR() {
        return OPENHOUR;
    }

    public String getENTR_FEE() {
        return ENTR_FEE;
    }

    public String getCLOSEDAY() {
        return CLOSEDAY;
    }

    public String getOPEN_DAY() {
        return OPEN_DAY;
    }

    public String getSEAT_CNT() {
        return SEAT_CNT;
    }

    public String getX_COORD() {
        return X_COORD;
    }

    public String getY_COORD() {
        return Y_COORD;
    }

    public String getETC_DESC() {
        return ETC_DESC;
    }

    public String getFAC_DESC() {
        return FAC_DESC;
    }

    public String getENTRFREE() {
        return ENTRFREE;
    }

    public static final Parcelable.Creator<Row> CREATOR = new Parcelable.Creator<Row>() {
        @Override
        public Row createFromParcel(Parcel parcel) {
            return new Row(parcel);
        }
        @Override
        public Row[] newArray(int size) {
            return new Row[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.MAIN_IMG);
        dest.writeString(this.FAC_NAME);
        dest.writeString(this.ADDR);
        dest.writeString(this.OPENHOUR);
        dest.writeString(this.CLOSEDAY);
        dest.writeString(this.HOMEPAGE);
        dest.writeString(this.PHNE);
        dest.writeString(this.FAC_DESC);
    }
}

class Result{
    private String CODE;
    private String MESSAGE;

    public String getCODE() {
        return CODE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }
}
