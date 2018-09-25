package kr.co.company.jjigawesome;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    RelativeLayout button_buy;
    Button button_qrcode;
    Button button_back;
    TextView textView_count;

    RelativeLayout couponListLayout;
    LayoutInflater inflater;
    View couponList1;
    TextView list1Name;
    ImageView[] progressBar1;
    View couponList2;
    TextView list2Name;
    ImageView[] progressBar2;
    RelativeLayout relativeLayout_count;

    TextView textView_drawer_name;
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

        //////////////////////////////////////////

        couponListLayout = (RelativeLayout) findViewById(R.id.coupon_list_layout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        couponList1 = inflater.inflate(R.layout.home_coupon_list,null);
        couponList1.setId(View.generateViewId());
        list1Name = (TextView) couponList1.findViewById(R.id.coupon_list_name);
        list1Name.setText("대림미술관 무료 입장권");
        TextView list1Num = (TextView) couponList1.findViewById(R.id.coupon_list_num);
        list1Num.setText("10개");
        progressBar1 = new ImageView[38];
        progressBar1[0] = couponList1.findViewById(R.id.progressbar_home_l);
        for(int i=1; i<37; i++)
        {
            String progressCenter = "progressbar_home_c_"+i;
            int resID = getResources().getIdentifier(progressCenter, "id", getPackageName());
            progressBar1[i] = ((ImageView) couponList1.findViewById(resID));
        }
        progressBar1[37] = couponList1.findViewById(R.id.progressbar_home_r);
        RelativeLayout.LayoutParams couponList1Params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        couponList1.setLayoutParams(couponList1Params);
        couponList1Params.addRule(RelativeLayout.ALIGN_PARENT_TOP,1);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.round(26 * dm.density);
        couponList1Params.topMargin = size;
        couponListLayout.addView(couponList1);

        //////////////////////////////////////////////////
        couponList2 = inflater.inflate(R.layout.home_coupon_list,null);
        couponList2.setId(View.generateViewId());
        list2Name = (TextView) couponList2.findViewById(R.id.coupon_list_name);
        list2Name.setText("국립국악원 무료 체험권");
        TextView list2Num = (TextView) couponList2.findViewById(R.id.coupon_list_num);
        list2Num.setText("20개");
        progressBar2 = new ImageView[38];
        progressBar2[0] = couponList2.findViewById(R.id.progressbar_home_l);
        for(int i=1; i<37; i++)
        {
            String progressCenter = "progressbar_home_c_"+i;
            int resID = getResources().getIdentifier(progressCenter, "id", getPackageName());
            progressBar2[i] = ((ImageView) couponList2.findViewById(resID));
        }
        progressBar2[37] = couponList2.findViewById(R.id.progressbar_home_r);

        RelativeLayout.LayoutParams couponList2Params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        couponList2.setLayoutParams(couponList2Params);
        couponList2Params.addRule(RelativeLayout.BELOW, couponList1.getId());
        size = Math.round(10 * dm.density);
        couponList2Params.topMargin = size;
        couponListLayout.addView(couponList2);

        ////////////////////
        makeProgressBar();

        //////////////////////////////////////

        button_buy = (RelativeLayout) findViewById(R.id.button_home_buy);
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
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buttonMyStamp = (Button) findViewById(R.id.button_drawer_mystamp);
        buttonMyStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Button buttonBuy = (Button) findViewById(R.id.button_drawer_buy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BuyStamp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button button_logout = (Button) findViewById(R.id.button_drawer_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
                mPrefs.edit().remove("member").apply();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        button_qrcode = (Button) findViewById(R.id.button_home_qrcode);
        button_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QrcodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });


       /* button_back = (Button) findViewById(R.id.button_home_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
                mPrefs.edit().remove("member").apply();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });*/

        relativeLayout_count = (RelativeLayout) findViewById(R.id.relativeLayout_home_count);
        relativeLayout_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postString = new PostString();
                postString.setToken(member.getToken());
                url = "http://18.218.187.138:3000/stamp/";
                json = gson.toJson(postString);
                new GetStampTask().execute(url, json);
            }
        });

        textView_count = (TextView) findViewById(R.id.textview_home_count);
        textView_count.setText(member.getStampCount() + "개");

        postString = new PostString();
        postString.setToken(member.getToken());
        url = "http://18.218.187.138:3000/stamp/";
        json = gson.toJson(postString);
        new GetStampTask().execute(url, json);

        TextView textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        textView_drawer_name.setText(member.getName());

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
                    member.setStampCount(response.getNumber());
                    SPtoObject.saveObject(mPrefs,member,"member");
                    textView_count.setText(member.getStampCount() + "개");
                    //////////////////////////////////////////////////
                    makeProgressBar();
                    //////////////////////
                } else {
                    this.cancel(true);
                    textView_count.setText(member.getStampCount() + "개");
                    //////////////////////////////////////////////////
                    makeProgressBar();
                   //////////////////////
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
        new GetStampTask().execute(url,json);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        member = ((Member) SPtoObject.loadObject(mPrefs, "member", Member.class));
        textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        if(textView_drawer_name!=null) {
            textView_drawer_name.setText(member.getName());
        }
    }
    protected  void onRestart() {
        super.onRestart();
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(
                getSupportFragmentManager()
        );
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(homePagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void makeProgressBar() {
        int stampCount = member.getStampCount();
        ImageView couponListOn = (ImageView) couponList1.findViewById(R.id.coupon_list_on);
        ImageView couponListCheck = (ImageView) couponList1.findViewById(R.id.coupon_list_check);
        if(stampCount < 10) {
            couponListOn.setVisibility(View.INVISIBLE);
            couponListCheck.setVisibility(View.INVISIBLE);
            list1Name.setTextColor(0xff9d9d9d);
        }
        else{
            couponListOn.setVisibility(View.VISIBLE);
            couponListCheck.setVisibility(View.VISIBLE);
            list1Name.setTextColor(0xff545454);
        }
        if(stampCount/(float)10 >= 1/(float)38)
            progressBar1[0].setImageResource(R.drawable.progressbar_home_sel_l);
        else
            progressBar1[0].setImageResource(R.drawable.progressbar_home_nor_l);
        for (int i=1; i<37; i++)
        {
            if(stampCount/(float)10 >= (i+1)/(float)38)
                progressBar1[i].setImageResource(R.drawable.progressbar_home_sel_c);
            else
                progressBar1[i].setImageResource(R.drawable.progressbar_home_nor_c);
        }
        if(stampCount/(float)10 >= 1)
            progressBar1[37].setImageResource(R.drawable.progressbar_home_sel_r);
        else
            progressBar1[37].setImageResource(R.drawable.progressbar_home_nor_r);
        RelativeLayout.LayoutParams couponList1Params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ////////////////
        ImageView couponList2On = (ImageView) couponList2.findViewById(R.id.coupon_list_on);
        ImageView couponList2Check = (ImageView) couponList2.findViewById(R.id.coupon_list_check);
        if(stampCount < 20) {
            couponList2On.setVisibility(View.INVISIBLE);
            couponList2Check.setVisibility(View.INVISIBLE);
            list2Name.setTextColor(0xff9d9d9d);
        }
        else{
            couponList2On.setVisibility(View.VISIBLE);
            couponList2Check.setVisibility(View.VISIBLE);
            list2Name.setTextColor(0xff545454);
        }
        if(stampCount/(float)20 >= 1/(float)38)
            progressBar2[0].setImageResource(R.drawable.progressbar_home_sel_l);
        else
            progressBar2[0].setImageResource(R.drawable.progressbar_home_nor_l);
        for (int i=1; i<37; i++)
        {
            if(stampCount/(float)20 >= (i+1)/(float)38)
                progressBar2[i].setImageResource(R.drawable.progressbar_home_sel_c);
            else
                progressBar2[i].setImageResource(R.drawable.progressbar_home_nor_c);
        }
        if(stampCount/(float)20 >= 1)
            progressBar2[37].setImageResource(R.drawable.progressbar_home_sel_r);
        else
            progressBar2[37].setImageResource(R.drawable.progressbar_home_nor_r);
    }
}
