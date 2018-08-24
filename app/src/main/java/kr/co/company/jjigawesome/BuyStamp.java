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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BuyStamp extends AppCompatActivity {
    int newUiOptions;
    View view;
    SharedPreferences mPrefs;
    Member member;
    RecyclerView recyclerView;
    List<Coupon> coupons = new ArrayList<>();
    Gson gson = new Gson();
    String url;
    String json;

    LinearLayout linearLayout;
    EditText editText;

    Button button_qrcode;
    TextView textView_buystamp;
    Button button_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_stamp);

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


        TextView textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        textView_buystamp = (TextView) findViewById(R.id.textview_buystamp);
        textView_drawer_name.setText(member.getName());
        textView_buystamp.setText("나의 스탬프 " + member.getStampCount()+ "개");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_buystamp);
        for(int i=0;i<3;i++){
            coupons.add(new Coupon(i));
        }
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView_buystamp.setText("나의 스탬프 " + member.getStampCount()+ "개");
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_buystamp);
        if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    private void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        BuyStamp.CouponAdapter adapter = new BuyStamp.CouponAdapter(coupons);
        recyclerView.setAdapter(adapter);
    }


    private class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{
        List<Coupon> coupons;

        public CouponAdapter(List<Coupon> coupons){
            this.coupons = coupons;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coupon,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Coupon coupon = coupons.get(position);
            final int type = coupon.getType();

            final PostString postString = new PostString();
            holder.button_coupon.setEnabled(false);
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
            }
            holder.button_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postString.setToken(member.getToken());
                    postString.setType(type);
                    url = Post.URL + "/stamp/exchange";
                    json = gson.toJson(postString);
                    new ExchangeStampTask().execute(url, json);
                }
            });

        }

        @Override
        public int getItemCount() {
            return coupons.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            Button button_coupon;
            public ViewHolder(View itemView) {
                super(itemView);
                button_coupon = itemView.findViewById(R.id.button_coupon);
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
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

    }
}
