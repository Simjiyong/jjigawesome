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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Method;

public class LoginActivity extends AppCompatActivity {

    int newUiOptions;
    View view;

    String url;
    String json;
    Gson gson = new Gson();

    EditText editText_id;
    EditText editText_password;

    CheckBox checkBox_keep;

    Button button_login;
    Button button_signup;

    Member loginMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        editText_id = (EditText) findViewById(R.id.edit_login_id);
        editText_password = (EditText) findViewById(R.id.edit_login_password);

        button_signup = (Button) findViewById(R.id.button_login_signup);
        button_login = (Button) findViewById(R.id.button_login);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPossible = true;
                String id = editText_id.getText().toString();

                switch (v.getId()) {
                    case R.id.button_login:

                        if (!ValidateForm.checkForm(editText_id, editText_password)) {
                            isPossible = false;
                        }
                        if (!id.matches(ValidateForm.EMAIL_REGEX)) {
                            editText_id.setError("이메일 형식이 아닙니다.");
                            isPossible = false;
                        }

                        if (isPossible == false) {
                            break;
                        }
                        loginMember = new Member(id, editText_password.getText().toString());
                        json = gson.toJson(loginMember);
                        new LoginTask().execute();
                        break;

                    case R.id.button_login_signup:
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                }
            }
        };

        button_login.setOnClickListener(onClickListener);
        button_signup.setOnClickListener(onClickListener);


    }

    private class LoginTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            url = "http://18.218.187.138:3000/login";
            Post post = new Post(url, json);
            String response = null;
            try {
                response = post.post();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("response" , response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Response response = gson.fromJson(s,Response.class);
            if(response!=null) {
                if (response.getStatus().equals("OK")) {
                    Toast.makeText(getApplicationContext(), "로그인 성공 했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "로그인 실패 입니다.", Toast.LENGTH_SHORT).show();
                    this.cancel(true);
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "오류! 서버로 부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
