package kr.co.company.jjigawesome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Method;

public class ChangePasswordActivity extends AppCompatActivity {
    int newUiOptions;
    View view;
    SharedPreferences mPrefs;
    String json;
    String url;
    Gson gson = new Gson();
    Member member;

    Button button_qrcode;
    Button button_finish;
    Button button_confirm;
    EditText editText_pw;
    EditText editText_repw;


    TextView textView_drawer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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

        Button buttonOpenDrawer = (Button) findViewById(R.id.button_change_password_menu);
        buttonOpenDrawer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_change_password);
                if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonCloseDrawer = (Button) findViewById(R.id.close_drawer_button);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_change_password);
                if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });

        Button buttonMyPage = (Button) findViewById(R.id.button_drawer_mypage);
        buttonMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buttonMyCoupon = (Button) findViewById(R.id.button_drawer_coupon);
        buttonMyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, MyStampActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button buttonBuy = (Button) findViewById(R.id.button_drawer_buy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, BuyStamp.class);
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
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        button_qrcode = (Button) findViewById(R.id.button_change_password_qrcode);
        button_confirm = (Button) findViewById(R.id.button_change_password_ok);
        button_finish = (Button) findViewById(R.id.button_change_password_back);
        editText_pw = (EditText) findViewById(R.id.edit_change_password);
        editText_repw = (EditText) findViewById(R.id.edit_change_password_re);

        TextView textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        textView_drawer_name.setText(member.getName());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPossible = true;

                switch (v.getId()) {
                    case R.id.button_change_password_qrcode:
                        Intent intent = new Intent(ChangePasswordActivity.this,  QrcodeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    case R.id.button_change_password_back:
                        finish();
                        break;
                    case R.id.button_change_password_ok:
                        if(!ValidateForm.checkForm(editText_pw,editText_repw)){
                            isPossible = false;
                        }
                        if(!editText_pw.getText().toString().equals(editText_repw.getText().toString())){
                            isPossible = false;
                        }

                        if(!isPossible){
                            break;
                        }
                        PostString postString = new PostString();
                        postString.setToken(member.getToken());
                        postString.setPassword(editText_pw.getText().toString());
                        url = Post.URL + "/find/reset";
                        json = gson.toJson(postString);
                        new ChangePWTask().execute(url,json);
                        break;
                }
            }
        };

        button_qrcode.setOnClickListener(onClickListener);
        button_finish.setOnClickListener(onClickListener);
        button_confirm.setOnClickListener(onClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        member = ((Member) SPtoObject.loadObject(mPrefs, "member", Member.class));
        textView_drawer_name = (TextView) findViewById(R.id.drawer_name);
        if(textView_drawer_name!=null) {
            textView_drawer_name.setText(member.getName());
        }
    }

    private class ChangePWTask extends PostTask{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                Log.d("response", s);
                Response response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    final MyDialog myDialog = new MyDialog(ChangePasswordActivity.this);
                    myDialog.setTextViewText("비밀번호 수정이 완료되었습니다.");
                    myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                            finish();
                        }
                    });
                    myDialog.show();
                } else {
                    this.cancel(true);
                    Toast.makeText(getApplicationContext(), "비밀번호 수정을 실패 했습니다.", Toast.LENGTH_SHORT).show();
                }

            }  catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


}
