package kr.co.company.jjigawesome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyStampActivity extends AppCompatActivity {
    int newUiOptions;
    View view;
    SharedPreferences mPrefs;
    Member member;
    Gson gson = new Gson();
    String url;
    String json;
    List<Coupon> coupons = new ArrayList<>();
    RecyclerView recyclerView;
    CouponAdapter adapter;
    MyDialog myDialog;

    int realWidth;
    int realHeight;

    TextView textView_mystamp;
    TextView textView_coupon_num;
    Button button_qrcode;
    Button button_finish;
    int tmpPosition;

    TextView textView_drawer_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stamp);

        mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
        member = (Member) SPtoObject.loadObject(mPrefs, "member", Member.class);

        Display display = this.getWindowManager().getDefaultDisplay();

        myDialog = new MyDialog(this);


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

        Button buttonOpenDrawer = (Button) findViewById(R.id.button_mystamp_menu);
        buttonOpenDrawer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_mystamp);
                if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonCloseDrawer = (Button) findViewById(R.id.close_drawer_button);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_mystamp);
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonMyPage = (Button) findViewById(R.id.button_drawer_mypage);
        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyStampActivity.this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buttonMyStamp = (Button) findViewById(R.id.button_drawer_mystamp);
        buttonMyStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyStampActivity.this, HomeActivity.class);
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
                Intent intent = new Intent(MyStampActivity.this, MyStampActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buttonBuy = (Button) findViewById(R.id.button_drawer_buy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyStampActivity.this, BuyStamp.class);
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
                Intent intent = new Intent(MyStampActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        TextView textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        textView_mystamp = (TextView) findViewById(R.id.textview_mystamp);
        textView_coupon_num = (TextView) findViewById(R.id.textview_mystamp_coupon_num);
        button_qrcode = (Button) findViewById(R.id.button_mystamp_qrcode);
        button_finish = (Button) findViewById(R.id.button_mypage_stamp);

        textView_drawer_name.setText(member.getName());
        textView_mystamp.setText("나의 스탬프 " + member.getStampCount() + "개");
        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyStampActivity.this, QrcodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_mystamp);
        PostString postString = new PostString();
        postString.setToken(member.getToken());
        url = Post.URL + "/stamp/hold";
        json = gson.toJson(postString);
        new GettingCouponTask().execute(url, json);
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CouponAdapter(coupons);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetStampTask(getApplicationContext(), mPrefs, member).execute();
        PostString postString = new PostString();
        postString.setToken(member.getToken());
        url = Post.URL + "/stamp/hold";
        json = gson.toJson(postString);
        new GettingCouponTask().execute(url, json);
        textView_mystamp.setText("나의 스탬프 " + member.getStampCount() + "개");
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_mystamp);
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        textView_coupon_num.setText(coupons.size() + "개");
        member = ((Member) SPtoObject.loadObject(mPrefs, "member", Member.class));
        textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        if(textView_drawer_name!=null) {
            textView_drawer_name.setText(member.getName());
        }
    }


    private class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
        List<Coupon> coupons = new ArrayList<>();

        public CouponAdapter(List<Coupon> coupons) {
            this.coupons = coupons;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyStampActivity.this).inflate(R.layout.layout_coupon, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Coupon coupon = coupons.get(position);
            int type = coupon.getType();

            /*ViewGroup.LayoutParams params = holder.button_coupon.getLayoutParams();
            params.width = (int)(((float)realWidth/1080) * 1041);
            params.height = (int)(((float)realWidth/1080) * 466);
            holder.button_coupon.setLayoutParams(params);*/

            holder.textView_couponName.setText(coupon.getStampname());
            Glide.with(getApplicationContext()).load(coupon.getLogo()).into(holder.imageView_couponLogo);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(coupon.getCreateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            Calendar createDay = Calendar.getInstance();
            createDay.setTime(date);
            cal.setTime(date);
            cal.add(Calendar.DATE, 90);
            String dateExpired = dateFormat.format(createDay.getTime()) + " ~ " + dateFormat.format(cal.getTime());

            holder.textView_couponExpired.setText(dateExpired);

            holder.button_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///////////////////////////////
                    final MyDialog myDialog = new MyDialog(MyStampActivity.this);
                    myDialog.setTextViewText("쿠폰을 사용하시겠습니까?");
                    myDialog.showCancelButton();
                    myDialog.getButton_cancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostString postString = new PostString();
                            postString.setToken(member.getToken());
                            postString.setNum(coupon.getNum());
                            url = Post.URL + "/stamp/email";
                            json = gson.toJson(postString);
                            new SendCouponTask().execute(url, json);
                            tmpPosition = position;
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                }
                ////////////////////////
                /*
                    PostString postString = new PostString();
                    postString.setToken(member.getToken());
                    postString.setNum(coupon.getNum());
                    url = Post.URL + "/stamp/email";
                    json = gson.toJson(postString);
                    new SendCouponTask().execute(url,json);
                    tmpPosition = position;*/
            });

        }

        @Override
        public int getItemCount() {
            return coupons.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            Button button_coupon;
            TextView textView_couponName;
            TextView textView_couponExpired;
            ImageView imageView_couponLogo;

            public ViewHolder(View itemView) {
                super(itemView);
                button_coupon = (Button) itemView.findViewById(R.id.button_coupon);
                textView_couponName = itemView.findViewById(R.id.textview_coupon_name);
                textView_couponExpired = itemView.findViewById(R.id.textview_coupon_expired);
                imageView_couponLogo = itemView.findViewById(R.id.image_coupon_logo);
            }
        }
    }

    private class GettingCouponTask extends PostTask {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("response", s);
                coupons = Arrays.asList(gson.fromJson(s, Coupon[].class));
                setRecyclerView();
                textView_coupon_num.setText(coupons.size() + "개");
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private class SendCouponTask extends PostTask {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);
                myDialog.setTextViewText("쿠폰을 메일로 보냈습니다.");
                myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                PostString postString = new PostString();
                postString.setToken(member.getToken());
                url = Post.URL + "/stamp/hold";
                json = gson.toJson(postString);
                new GettingCouponTask().execute(url, json);
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

}
