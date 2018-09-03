package kr.co.company.jjigawesome;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Method;

public class FindPWActivity extends AppCompatActivity {

    int newUiOptions;
    View view;
    Gson gson = new Gson();
    Response response;

    boolean isConfirm;
    String url;
    String json;

    EditText editText_email;
    EditText editText_id;
    EditText editText_num;
    Button button_sendNum;
    Button button_confirm;
    Button button_finish;
    Button button_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

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

        editText_email = (EditText) findViewById(R.id.edit_findpw_email);
        editText_id = (EditText) findViewById(R.id.edit_findpw_id);
        editText_num = (EditText) findViewById(R.id.edit_findpw_confirm);
        button_confirm = (Button) findViewById(R.id.button_findpw_confirm);
        button_sendNum = (Button) findViewById(R.id.button_findpw_sendnumber);
        button_finish = (Button) findViewById(R.id.button_findpw_finish);
        button_reset = (Button) findViewById(R.id.button_findpw_reset);

        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean isPossible = true;
                PostString postString;
                switch(v.getId()){
                    case R.id.button_findpw_finish:
                        finish();
                        break;
                    case R.id.button_findpw_confirm:
                        if(!ValidateForm.checkForm(editText_num, editText_email)){
                            isPossible = false;
                        }

                        if(isPossible == false){
                            break;
                        }
                        postString = new PostString();
                        postString.setAuth_num(Integer.parseInt(editText_num.getText().toString()));
                        postString.setEmail(editText_email.getText().toString());
                        json = gson.toJson(postString);
                        new CheckTask().execute();
                        break;
                    case R.id.button_findpw_sendnumber:
                        if(!ValidateForm.checkForm(editText_id)){
                            isPossible = false;
                        }
                        if(!ValidateForm.checkForm(editText_email)){
                            isPossible = false;
                        }
                        if(!editText_email.getText().toString().matches(ValidateForm.EMAIL_REGEX)){
                            editText_email.setError("이메일 형식이 아닙니다.");
                            isPossible = false;
                        }

                        if(!isPossible){
                            break;
                        }
                        postString = new PostString();
                        postString.setEmail(editText_email.getText().toString());
                        postString.setId(editText_id.getText().toString());
                        json = gson.toJson(postString);
                        Log.e("postString", json);
                        new PasswordTask().execute();
                        break;
                    case R.id.button_findpw_reset:
                        if(!ValidateForm.checkForm(editText_id)){
                            isPossible = false;
                        }
                        if(!ValidateForm.checkForm(editText_email)){
                            isPossible = false;
                        }
                        if(!isConfirm){
                            editText_num.setError("인증이 필요합니다.");
                            Toast.makeText(getApplicationContext(),"인증을 완료 해주세요.", Toast.LENGTH_SHORT).show();
                            isPossible = false;
                        }
                        if(!isPossible){
                            break;
                        }
                        Intent intent = new Intent(FindPWActivity.this, ResetPWActivity.class);
                        intent.putExtra("email", editText_email.getText().toString());
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };

        button_reset.setOnClickListener(onClickListener);
        button_finish.setOnClickListener(onClickListener);
        button_sendNum.setOnClickListener(onClickListener);
        button_confirm.setOnClickListener(onClickListener);
    }

    private class AuthTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            url = "http://18.218.187.138:3000/find/auth";

            Post post = new Post(url, json);
            String response = null;
            try {
                response = post.post();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.d("findpw" , "Auth!!");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    final MyDialog myDialog = new MyDialog(FindPWActivity.this);
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
            Log.d("findpw" , "Check!!");
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

    private class PasswordTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            url = "http://18.218.187.138:3000/find/password";

            Post post = new Post(url, json);
            String response = null;
            try {
                response = post.post();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.d("findpw" , "Password!!");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    new AuthTask().execute();
                } else {
                    this.cancel(true);
                    final MyDialog myDialog = new MyDialog(FindPWActivity.this);
                    myDialog.setTextViewText("아이디와 이메일이 일치하지 않습니다.");
                    myDialog.getButton_confirm().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
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
