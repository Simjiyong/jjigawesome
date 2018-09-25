package kr.co.company.jjigawesome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MypageActivity extends AppCompatActivity {
    int newUiOptions;
    View view;
    SharedPreferences mPrefs;
    Member member;

    TextView textView_name;
    TextView textView_email;
    TextView textView_phone;
    Button button_finish;
    Button button_qrcode;
    RelativeLayout button_name;
    RelativeLayout button_pw;

    DrawerLayout drawerLayout;

    TextView textView_drawer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Display display = this.getWindowManager().getDefaultDisplay();
        int realWidth;
        int realHeight;

        if (Build.VERSION.SDK_INT >= 17) {
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

        if (realWidth * 16 == realHeight * 9) {
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


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_mypage);
        Button buttonOpenDrawer = (Button) findViewById(R.id.button_mypage_menu);
        buttonOpenDrawer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonCloseDrawer = (Button) findViewById(R.id.close_drawer_button);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonMyPage = (Button) findViewById(R.id.button_drawer_mypage);
        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buttonMyStamp = (Button) findViewById(R.id.button_drawer_mystamp);
        buttonMyStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Button buttonMyCoupon = (Button) findViewById(R.id.button_drawer_coupon);
        buttonMyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                intent = new Intent(MypageActivity.this, MyStampActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        Button buttonBuy = (Button) findViewById(R.id.button_drawer_buy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                intent = new Intent(MypageActivity.this, BuyStamp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        Button button_logout = (Button) findViewById(R.id.button_drawer_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
                mPrefs.edit().remove("member").apply();
                Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
        member = (Member) SPtoObject.loadObject(mPrefs,"member", Member.class);

        textView_name = (TextView) findViewById(R.id.textview_mypage_name);
        textView_email = (TextView) findViewById(R.id.textview_mypage_email);
        textView_phone = (TextView) findViewById(R.id.textview_mypage_phone);
        button_finish = (Button) findViewById(R.id.button_mypage_back);
        button_qrcode = (Button) findViewById(R.id.button_mypage_qrcode);
        button_name = (RelativeLayout) findViewById(R.id.button_mypage_change_name);
        button_pw = (RelativeLayout) findViewById(R.id.button_mypage_change_password);

        TextView textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        textView_drawer_name.setText(member.getName());
        textView_email.setText(member.getEmail());
        textView_name.setText(member.getName());
        textView_phone.setText("");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()){
                    case R.id.button_mypage_back:
                        finish();
                        break;
                    case R.id.button_mypage_qrcode:
                        intent = new Intent(MypageActivity.this, QrcodeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    case R.id.button_mypage_change_name:
                        intent = new Intent(MypageActivity.this, ChangeNameActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    case R.id.button_mypage_change_password:
                        intent = new Intent(MypageActivity.this,CheckPWActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                }
            }
        };

        button_finish.setOnClickListener(onClickListener);
        button_pw.setOnClickListener(onClickListener);
        button_name.setOnClickListener(onClickListener);
        button_qrcode.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        member = ((Member) SPtoObject.loadObject(mPrefs, "member", Member.class));
        textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        if(textView_drawer_name!=null) {
            textView_drawer_name.setText(member.getName());

        }

        if(textView_name!=null) {
            textView_name.setText(member.getName());
        }
    }
}
