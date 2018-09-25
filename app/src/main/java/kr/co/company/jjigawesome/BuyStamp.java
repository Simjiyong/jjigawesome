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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuyStamp extends AppCompatActivity {
    int newUiOptions;
    View view;
    SharedPreferences mPrefs;
    Member member;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    BuyStamp.CouponAdapter adapter;
    List<Coupon> coupons = new ArrayList<>();
    Gson gson = new Gson();
    String url;
    String json;
    int realWidth;
    int realHeight;
    ImageView[] imageViews = new ImageView[15];

    LinearLayout linearLayout;
    EditText editText;

    Button button_qrcode;
    TextView textView_buystamp;
    Button button_finish;
    Button button_tmp;
    TextView textView_drawer_name;

    public static int index = -1;
    public static int top = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_stamp);

        mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
        member=((Member)SPtoObject.loadObject(mPrefs,"member",Member.class));

        Display display = this.getWindowManager().getDefaultDisplay();


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

        Button buttonOpenDrawer = (Button) findViewById(R.id.button_buystamp_menu);
        buttonOpenDrawer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_buystamp);
                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonCloseDrawer = (Button) findViewById(R.id.close_drawer_button);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_buystamp);
                if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonMyPage = (Button) findViewById(R.id.button_drawer_mypage);
        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyStamp.this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buttonMyStamp = (Button) findViewById(R.id.button_drawer_mystamp);
        buttonMyStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyStamp.this, HomeActivity.class);
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
                Intent intent = new Intent(BuyStamp.this, MyStampActivity.class);
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
                Intent intent = new Intent(BuyStamp.this, BuyStamp.class);
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
                Intent intent = new Intent(BuyStamp.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        button_qrcode = (Button) findViewById(R.id.button_buystamp_qrcode);
        button_finish = (Button) findViewById(R.id.button_buystamp_back);

        button_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyStamp.this,  QrcodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        textView_buystamp = (TextView) findViewById(R.id.textview_buystamp);
        textView_drawer_name.setText(member.getName());
        textView_buystamp.setText("나의 스탬프 " + member.getStampCount()+ "개");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_buystamp);
        for(int i=0;i<3;i++){
            coupons.add(new Coupon(i));
        }
        PostString postString = new PostString();
        url = Post.URL+"/stamp/info";
        json = gson.toJson(postString);
        new GetCouponListTask().execute(url,json);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PostString postString = new PostString();
        postString.setToken(member.getToken());
        url = "http://18.218.187.138:3000/stamp/";
        json = gson.toJson(postString);
        new GetStampTask().execute(url, json);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_buystamp);
        if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        member = ((Member) SPtoObject.loadObject(mPrefs, "member", Member.class));
        textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        if(textView_drawer_name!=null) {
            textView_drawer_name.setText(member.getName());
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(layoutManager !=null) {
            index = layoutManager.findFirstVisibleItemPosition();
            View v = recyclerView.getChildAt(0);
            top = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop());
        }
    }

    private void setRecyclerView(){
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BuyStamp.CouponAdapter(coupons);
        recyclerView.setAdapter(adapter);

    }


    private class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{
        List<Coupon> coupons;

        public CouponAdapter(List<Coupon> coupons){
            this.coupons = coupons;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_buy_coupon,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final ViewHolder holder1 = holder;
            final Coupon coupon = coupons.get(position);
            final int type = coupon.getType();

            /*ViewGroup.LayoutParams params = holder.button_coupon.getLayoutParams();
            params.width = (int)(((float)realWidth/1080) * 1041);
            params.height = (int)(((float)realWidth/1080) * 466);
            holder.button_coupon.setLayoutParams(params);*/

            final PostString postString = new PostString();
            /*holder.button_coupon.setEnabled(false);
            if(type == 0) {
                holder.button_coupon.setBackgroundResource(R.drawable.img_seoulbike_off);
                if(member.getStampCount()>=5){
                    holder.button_coupon.setBackgroundResource(R.drawable.img_seoulbike_on);
                    holder.button_coupon.setEnabled(true);
                    postString.setStamp(5);
                }
            }
            else if(type == 1){
                holder.button_coupon.setBackgroundResource(R.drawable.img_palace_off);
                if(member.getStampCount()>=20){
                    holder.button_coupon.setBackgroundResource(R.drawable.img_palace_on);
                    holder.button_coupon.setEnabled(true);
                    postString.setStamp(20);
                }
            }
            else if(type == 2){
                holder.button_coupon.setBackgroundResource(R.drawable.img_museum_off);
                if(member.getStampCount()>=30){
                    holder.button_coupon.setBackgroundResource(R.drawable.img_museum_on);
                    holder.button_coupon.setEnabled(true);
                    postString.setStamp(30);
                }
           }*/
            Glide.with(getApplicationContext()).load(coupon.getLogo()).into(holder.imageView_coupon);

            if(member.getStampCount()<coupon.getStamp_number()){
                holder.button_coupon.setEnabled(false);
                holder.linearLayout_coupon.setBackgroundResource(R.drawable.img_ticket_off);
            }
            else{
                holder.button_coupon.setEnabled(true);
                holder.linearLayout_coupon.setBackgroundResource(R.drawable.img_ticket_on);
            }
            holder.textView_couponName.setText(coupon.getCouponname());
            holder.textView_couponName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BuyStamp.this, CouponDetailActivity.class);
                    intent.putExtra("data", coupon.getData());
                    startActivity(intent);
                }
            });
            holder.button_coupon.setText(String.valueOf(coupon.getStamp_number() + "개"));
            holder.button_coupon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final MyDialog myDialog = new MyDialog(BuyStamp.this);
                    myDialog.setTextViewText("쿠폰을 교환하시겠습니까?");
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
                            button_tmp = holder1.button_coupon;
                            postString.setToken(member.getToken());
                            postString.setStamp(coupon.getStamp_number());
                            postString.setType(type);
                            url = Post.URL + "/stamp/exchange";
                            json = gson.toJson(postString);
                            new ExchangeStampTask().execute(url, json);
                            myDialog.dismiss();
                        }
                    });

                    myDialog.show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return coupons.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView_coupon;
            LinearLayout linearLayout_coupon;
            Button button_coupon;
            TextView textView_couponName;
            public ViewHolder(View itemView) {
                super(itemView);
                linearLayout_coupon = itemView.findViewById(R.id.linear_coupon);
                textView_couponName = itemView.findViewById(R.id.textview_coupon_name);
                button_coupon = itemView.findViewById(R.id.button_coupon);
                imageView_coupon = itemView.findViewById(R.id.image_coupon);
            }
        }
    }

    private class ExchangeStampTask extends PostTask{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    PostString postString = new PostString();
                    postString.setToken(member.getToken());
                    url = "http://18.218.187.138:3000/stamp/";
                    json = gson.toJson(postString);
                    Log.e("postString", json);
                    new GetStampTask().execute(url, json);

                } else {
                    this.cancel(true);
                }
            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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
                    textView_buystamp.setText("나의 스탬프 " + member.getStampCount() + "개");

                    setRecyclerView();

                } else {
                    this.cancel(true);
                    textView_buystamp.setText("나의 스탬프 " + member.getStampCount() + "개");
                }
            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            if(index != -1)
            {
                layoutManager.scrollToPositionWithOffset(index, top);
            }
        }
    }

    private class GetCouponListTask extends PostTask{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("response", s);
                coupons = Arrays.asList(gson.fromJson(s, Coupon[].class));
                for(int i=0;i<coupons.size();i++){
                    new GetApiDataTask(coupons.get(i),i).execute(Post.API_URL+coupons.get(i).getKey());
                }
                setRecyclerView();

            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    private class GetApiDataTask extends PostAPITask{

        Coupon coupon;
        int position;

        GetApiDataTask(Coupon coupon, int position){
            this.coupon = coupon;
            this.position = position;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("response", s);
                Gson gson = new Gson();
                apiResponse = gson.fromJson(s, kr.co.company.jjigawesome.Response.class);
                coupon.setData(apiResponse.getSearchCulturalFacilitiesDetailService().getRow().get(0));
                //Glide.with(getApplicationContext()).load(coupon.getData().getMAIN_IMG()).into(imageViews[position]);

            } catch (NullPointerException e){

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
