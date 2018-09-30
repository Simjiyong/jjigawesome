package kr.co.company.jjigawesome;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Method;

public class CouponDetailActivity extends AppCompatActivity {
    int newUiOptions;
    View view;
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
        Display display = this.getWindowManager().getDefaultDisplay();
        int realWidth;
        int realHeight;

        if (Build.VERSION.SDK_INT >= 17){
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realWidth = realMetrics.widthPixels;
            realHeight = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                realWidth = display.getWidth();
                realHeight = display.getHeight();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            realWidth = display.getWidth();
            realHeight = display.getHeight();
        }

        if(realWidth * 16 == realHeight * 9){
            view = getWindow().getDecorView();
            int uiOptions = view.getSystemUiVisibility();
            newUiOptions = uiOptions;
            boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
            if (isImmersiveModeEnabled) {
                Log.i("Is on?", "Turning immersive mode mode off. ");
            } else {
                Log.i("Is on?", "Turning immersive mode mode on.");
            }
            newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;

            view.setSystemUiVisibility(newUiOptions);
            view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    view.setSystemUiVisibility(newUiOptions);
                }
            });
        }

        setContentView(R.layout.activity_coupon_detail);
        setTheme(android.R.style.Theme_Translucent_NoTitleBar);
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.5f;
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setAttributes(layoutParams);


        Row data = (Row) getIntent().getParcelableExtra("data");

        couponImage = (ImageView) findViewById(R.id.detail_coupon_image);
        couponName = (TextView) findViewById(R.id.detail_coupon_name);
        couponAddr = (TextView) findViewById(R.id.detail_coupon_addr);
        couponOpenHour = (TextView) findViewById(R.id.detail_coupon_time);
        couponCloseDay = (TextView) findViewById(R.id.detail_coupon_holiday);
        couponHomepage = (TextView) findViewById(R.id.detail_coupon_homepage);
        couponPhne = (TextView) findViewById(R.id.detail_coupon_phoneNum);
        couponInst = (TextView) findViewById(R.id.detail_coupon_intro);

        if(data !=null && data.getMAIN_IMG() != null) {
            Glide.with(this).load(data.getMAIN_IMG()).into(couponImage);
            couponImage.setScaleType(ImageView.ScaleType.FIT_XY);
            couponName.setText(data.getFAC_NAME());
            couponAddr.setText(data.getADDR());
            couponOpenHour.setText(data.getOPENHOUR());
            couponCloseDay.setText(data.getCLOSEDAY());
            couponHomepage.setText(data.getHOMEPAGE());
            couponPhne.setText(data.getPHNE());
            couponInst.setText(data.getFAC_DESC());
        }
        else{
            Log.e("error", "그림과 data가 null이 아닐때");
            finish();
        }

        Button closeButton = (Button) findViewById(R.id.button_popup_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
