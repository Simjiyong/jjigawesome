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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    ImageView imageView_bg;
    EditText editText_name;
    EditText editText_id;
    EditText editText_password;
    EditText editText_repassword;
    EditText editText_email;
    EditText editText_confirm;
    RadioGroup radioGroup_mem;
    Button button_finish;
    Button button_sendNum;
    Button button_signup;
    Button button_confirm;


    Boolean isConfirm = false;
    Member signUpMember;
    Response response;

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

        radioGroup_mem = (RadioGroup) findViewById(R.id.radio);

        editText_name = (EditText) findViewById(R.id.edit_signup_name);
        editText_id = (EditText) findViewById(R.id.edit_signup_id);
        editText_password = (EditText) findViewById(R.id.edit_signup_password);
        editText_repassword = (EditText) findViewById(R.id.edit_signup_passwordcheck);
        editText_email = (EditText) findViewById(R.id.edit_signup_email);
        editText_confirm = (EditText) findViewById(R.id.edit_signup_confirm);

        button_finish = (Button) findViewById(R.id.button_signup_finish);
        button_sendNum = (Button) findViewById(R.id.button_signup_sendnumber);
        button_signup = (Button) findViewById(R.id.button_signup);
        button_confirm = (Button) findViewById(R.id.button_signup_confirm);

        imageView_bg = (ImageView) findViewById(R.id.image_signup_bg);

        Glide.with(this).load(R.drawable.bg_signup).override(1080,1920).into(imageView_bg);

        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();
                boolean isPossible = true;

                PostString postString;

                switch(v.getId()){

                    case R.id.button_signup_finish:
                        finish();
                        break;

                    case R.id.button_signup_sendnumber:
                        if(!ValidateForm.checkForm(editText_email)){
                            isPossible = false;
                        }

                        if(isPossible == false){
                            break;
                        }
                        postString = new PostString();
                        postString.setEmail(editText_email.getText().toString());
                        json = gson.toJson(postString);
                        new AuthTask().execute();
                        break;

                    case R.id.button_signup:
                        EditText[] editTexts = {editText_name, editText_id, editText_password, editText_repassword, editText_email, editText_confirm};
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
                        if(!email.matches(ValidateForm.EMAIL_REGEX)){
                            editText_email.setError("이메일 형식이 아닙니다.");
                            isPossible = false;
                        }
                        if(!password.matches(ValidateForm.PASSWORD_REGEX)){
                            editText_password.setError("최소 8자리에 숫자, 문자, 특수문자 각각 1개 이상 포함해야 합니다.");
                            isPossible = false;
                        }

                        if(!isPossible){
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
                        if(!ValidateForm.checkForm(editText_confirm,editText_email)){
                            isPossible = false;
                        }

                        if(!isPossible){
                            break;
                        }
                        postString = new PostString();
                        postString.setEmail(editText_email.getText().toString());
                        postString.setAuth_num(Integer.parseInt(editText_confirm.getText().toString()));
                        json = gson.toJson(postString);
                        new CheckTask().execute();
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
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    Toast.makeText(getApplicationContext(), "회원가입이 성공 했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.getStatus().equals("ERROR_EMAIL")) {
                    this.cancel(true);
                    final MyDialog myDialog = new MyDialog(SignUpActivity.this);
                    myDialog.setTextViewText("이메일이 중복되었습니다.");
                    myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                    isConfirm = false;
                    button_confirm.setBackground(getDrawable(R.drawable.btn_phone_nor));
                } else if (response.getStatus().equals("ERROR_ID")) {
                    this.cancel(true);
                    final MyDialog myDialog = new MyDialog(SignUpActivity.this);
                    myDialog.setTextViewText("아이디가 중복되었습니다.");
                    myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                    isConfirm = false;
                    button_confirm.setBackground(getDrawable(R.drawable.btn_phone_nor));
                }
                else {
                    this.cancel(true);
                    final MyDialog myDialog = new MyDialog(SignUpActivity.this);
                    myDialog.setTextViewText("회원가입을 실패했습니다.");
                    myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                    isConfirm = false;
                    button_confirm.setBackground(getDrawable(R.drawable.btn_phone_nor));
                }
            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    private class AuthTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            url = "http://18.218.187.138:3000/find/auth/register";

            Post post = new Post(url, json);
            String response = null;
            try {
                response = post.post();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.d("signup" , "Auth!!");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    final MyDialog myDialog = new MyDialog(SignUpActivity.this);
                    myDialog.setTextViewText("이메일로 인증번호를 전송했습니다.");
                    myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });
                    myDialog.show();

                } else {
                    this.cancel(true);
                    //MyDialog myDialog = new MyDialog(SignUpActivity.this);
                    //myDialog.show();
                    Toast.makeText(getApplicationContext(), "인증번호 전송을 실패 했습니다.", Toast.LENGTH_SHORT).show();
                }

            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    private class CheckTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            url = "http://18.218.187.138:3000/find/check";

            Post post = new Post(url, json);
            String response = null;
            try {
                response = post.post();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.d("signup" , "Check!!");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    Toast.makeText(getApplicationContext(), "인증을 성공 했습니다.", Toast.LENGTH_SHORT).show();
                    isConfirm = true;
                    button_confirm.setBackground(getDrawable(R.drawable.btn_phone_sel));
                } else {
                    this.cancel(true);
                    Toast.makeText(getApplicationContext(), "인증을 실패 했습니다.", Toast.LENGTH_SHORT).show();
                    isConfirm = false;
                    button_confirm.setBackground(getDrawable(R.drawable.btn_phone_nor));
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