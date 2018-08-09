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

public class ResetPWActivity extends AppCompatActivity {

    int newUiOptions;
    View view;
    String json;
    String url;
    Gson gson = new Gson();
    Response response;

    Button button_confirm;
    Button button_finish;
    EditText editText_pw;
    EditText editText_repw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pw);

        Display display = this.getWindowManager().getDefaultDisplay();
        int realWidth;
        int realHeight;
        final Intent intent = getIntent();

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

        button_confirm = (Button) findViewById(R.id.button_reset_confirm);
        button_finish = (Button) findViewById(R.id.button_reset_finish);
        editText_pw = (EditText) findViewById(R.id.edit_reset_pw);
        editText_repw = (EditText) findViewById(R.id.edit_reset_repw);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPossible = true;
                PostString postString;

                switch (v.getId()){
                    case R.id.button_reset_confirm:
                        if(!ValidateForm.checkForm(editText_pw,editText_repw)){
                            isPossible = false;
                        }
                        if (!editText_pw.getText().toString().equals(editText_repw.getText().toString())) {
                            editText_repw.setError("비밀번호가 다릅니다.");
                            isPossible = false;
                        }
                        if(!isPossible){
                            break;
                        }
                        postString = new PostString();
                        postString.setPassword(editText_pw.getText().toString());
                        postString.setEmail(intent.getStringExtra("email"));
                        json = gson.toJson(postString);
                        new ResetTask().execute();
                        break;
                    case R.id.button_reset_finish:
                        finish();
                        break;
                }
            }
        };
        button_finish.setOnClickListener(onClickListener);
        button_confirm.setOnClickListener(onClickListener);
    }

    private class ResetTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            url = "http://18.218.187.138:3000/find/reset";

            Post post = new Post(url, json);
            String response = null;
            try {
                response = post.post();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("reset", "Reset!!");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {final MyDialog myDialog = new MyDialog(ResetPWActivity.this);
                    myDialog.setTextViewText("비밀번호 재설정이 완료되었습니다.");
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
                    Toast.makeText(getApplicationContext(), "비밀번호 재설정을 실패 했습니다.", Toast.LENGTH_SHORT).show();
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
