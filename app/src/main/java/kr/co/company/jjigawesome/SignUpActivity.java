package kr.co.company.jjigawesome;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Method;

public class SignUpActivity extends AppCompatActivity {

    int newUiOptions;
    View view;

    Gson gson = new Gson();
    String url;
    String json;

    LinearLayout linearLayout;
    EditText editText_name;
    EditText editText_id;
    EditText editText_password;
    EditText editText_repassword;
    EditText editText_phone;
    EditText editText_confirm;
    RadioGroup radioGroup_mem;
    Button button_finish;
    Button button_sendNum;
    Button button_signup;
    Button button_confirm;

    Boolean isConfirm = false;
    Member signUpMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

        linearLayout = (LinearLayout) findViewById(R.id.linear_signup);
        radioGroup_mem = (RadioGroup) findViewById(R.id.radio);

        editText_name = (EditText) findViewById(R.id.edit_signup_name);
        editText_id = (EditText) findViewById(R.id.edit_signup_id);
        editText_password = (EditText) findViewById(R.id.edit_signup_password);
        editText_repassword = (EditText) findViewById(R.id.edit_signup_passwordcheck);
        editText_phone = (EditText) findViewById(R.id.edit_signup_phone);
        editText_confirm = (EditText) findViewById(R.id.edit_signup_confirm);

        button_finish = (Button) findViewById(R.id.button_signup_finish);
        button_sendNum = (Button) findViewById(R.id.button_signup_sendnumber);
        button_signup = (Button) findViewById(R.id.button_signup);
        button_confirm = (Button) findViewById(R.id.button_signup_confirm);

        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = editText_id.getText().toString();

                switch(v.getId()){

                    case R.id.button_signup_finish:
                        finish();
                        break;

                    case R.id.button_signup_sendnumber:
                        if(!ValidateForm.checkForm(editText_id)){
                            break;
                        }

                        break;

                    case R.id.button_signup:
                        boolean isPossible = true;
                        EditText[] editTexts = {editText_name, editText_id, editText_password, editText_repassword, editText_phone, editText_confirm};
                        String[] strings = new String[6];
                        if(!ValidateForm.checkForm(editTexts)){
                            isPossible =false;
                        }
                        if (!editText_password.getText().toString().equals(editText_repassword.getText().toString())) {
                            editText_repassword.setError("비밀번호가 다릅니다.");
                            isPossible = false;
                        }
                        if(!isConfirm){
                            editText_confirm.setError("인증이 필요합니다.");
                            Toast.makeText(getApplicationContext(),"인증을 완료 해주세요.", Toast.LENGTH_SHORT).show();
                            isPossible = false;
                        }
                        if(!id.matches(ValidateForm.EMAIL_REGEX)){
                            editText_id.setError("이메일 형식이 아닙니다.");
                            isPossible = false;
                        }

                        if(isPossible == false){
                            break;
                        }
                        for(int i=0;i<editTexts.length;i++){
                            strings[i] = editTexts[i].getText().toString();
                        }
                        int type;
                        if(radioGroup_mem.getCheckedRadioButtonId() == R.id.radio_signup_member){
                            type = 0;
                        }
                        else{
                            type = 1;
                        }

                        signUpMember = new Member(strings[0],strings[1],strings[2],strings[4],type);
                        json = gson.toJson(signUpMember);
                        Log.d("signup", "url : "+ url + "json : " + json);
                        new SignUpTask().execute();
                        break;

                    case R.id.button_signup_confirm:
                        if(!ValidateForm.checkForm(editText_confirm)){
                            break;
                        }
                        if(editText_confirm.getText().toString().equals("1234")){
                            isConfirm = true;
                            button_confirm.setBackground(getDrawable(R.drawable.btn_phone_sel));
                        }
                        else{
                            isConfirm = false;
                            button_confirm.setBackground(getDrawable(R.drawable.btn_phone_nor));
                        }
                        break;
                }
            }
        };

        button_finish.setOnClickListener(clickListener);
        button_signup.setOnClickListener(clickListener);
        button_sendNum.setOnClickListener(clickListener);
        button_confirm.setOnClickListener(clickListener);

        /*Intent intent = new Intent(SignUpActivity.this, QrcodeActivity.class);
        startActivity(intent);*/
    }

    private class SignUpTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            url = "http://18.218.187.138:3000/register";
            Post post = new Post(url, json);
            String response = null;
            try {
                response = post.post();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.d("signup" , "signup!!");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Response response = gson.fromJson(s,Response.class);
            if(response!=null) {
                if (response.getStatus().equals("OK")) {
                    Toast.makeText(getApplicationContext(), "회원가입이 성공 했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "중복된 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    this.cancel(true);
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "오류! 서버로 부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            }
        }

    }

}