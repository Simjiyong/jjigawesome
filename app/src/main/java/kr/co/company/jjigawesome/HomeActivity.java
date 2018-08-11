package kr.co.company.jjigawesome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Method;

public class HomeActivity extends AppCompatActivity {
    int newUiOptions;
    View view;
    Gson gson = new Gson();
    String url;
    String json;
    SharedPreferences mPrefs;
    Member member;
    PostString postString;

    LinearLayout linearLayout;
    EditText editText;
    Button button_buy;
    Button button_qrcode;
    Button button_back;
    TextView textView_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
        member=((Member)SPtoObject.loadObject(mPrefs,"member",Member.class));

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

        button_buy = (Button) findViewById(R.id.button_home_buy);
        button_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BuyStamp.class);
                startActivity(intent);
            }
        });

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(
                getSupportFragmentManager()
        );
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(homePagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        Button buttonOpenDrawer = (Button) findViewById(R.id.button_home_menu);
        buttonOpenDrawer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home);
                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonCloseDrawer = (Button) findViewById(R.id.close_drawer_button);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home);
                if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonMyPage = (Button) findViewById(R.id.button_drawer_mypage);
        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });

        Button buttonMyCoupon = (Button) findViewById(R.id.button_drawer_coupon);
        buttonMyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyStampActivity.class);
                startActivity(intent);
            }
        });

        Button buttonBuy = (Button) findViewById(R.id.button_drawer_buy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BuyStamp.class);
                startActivity(intent);
            }
        });

        button_qrcode = (Button) findViewById(R.id.button_home_qrcode);
        button_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QrcodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        button_back = (Button) findViewById(R.id.button_home_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
                mPrefs.edit().remove("member").apply();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textView_count = (TextView) findViewById(R.id.textview_home_count);
        postString = new PostString();
        postString.setToken(member.getToken());
        url = "http://18.218.187.138:3000/stamp/";
        json = gson.toJson(postString);
        new GetStampTask().execute(url, json);

    }

    private class GetStampTask extends PostTask{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    textView_count.setText(response.getNumber() + "개");
                } else {
                    this.cancel(true);
                    textView_count.setText("개");

                }

            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetStampTask().execute(url, json);
    }
}
