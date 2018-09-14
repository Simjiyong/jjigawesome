package kr.co.company.jjigawesome;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class CouponDetailActivity extends AppCompatActivity {
    ImageView couponImage;
    TextView couponName;
    TextView couponAddr;
    TextView couponOpenHour;
    TextView couponCloseDay;
    TextView couponHomepage;
    TextView couponPhne;
    TextView couponInst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        Row data = (Row) getIntent().getParcelableExtra("data");
        couponImage = (ImageView) findViewById(R.id.detail_coupon_image);
        couponName = (TextView) findViewById(R.id.detail_coupon_name);
        couponAddr = (TextView) findViewById(R.id.detail_coupon_addr);
        couponOpenHour = (TextView) findViewById(R.id.detail_coupon_time);
        couponCloseDay = (TextView) findViewById(R.id.detail_coupon_holiday);
        couponHomepage = (TextView) findViewById(R.id.detail_coupon_homepage);
        couponPhne = (TextView) findViewById(R.id.detail_coupon_phoneNum);
        couponInst = (TextView) findViewById(R.id.detail_coupon_intro);

        couponName.setText(data.getFAC_NAME());
        couponAddr.setText(data.getADDR());
        couponOpenHour.setText(data.getOPENHOUR());
        couponCloseDay.setText(data.getCLOSEDAY());
        couponHomepage.setText(data.getHOMEPAGE());
        couponPhne.setText(data.getPHNE());
        couponInst.setText(data.getFAC_DESC());
    }
    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first)
    {
        super.onApplyThemeResource(theme, resid, first);
        theme.applyStyle(android.R.style.Theme_Panel, true);
    }
}
