package kr.co.company.jjigawesome;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Method;

public class ManagerIdActivity extends AppCompatActivity {
    int newUiOptions;
    View view;
    LinearLayout linearLayout;
    Gson gson = new Gson();
    String url;
    String json;

    SharedPreferences mPrefs;
    Member member;

    TextView textView_manager_name;
    EditText editText_id;
    Button button_confirm;
    Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_id);
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

        mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
        member=((Member)SPtoObject.loadObject(mPrefs,"member",Member.class));

        textView_manager_name = (TextView) findViewById(R.id.textview_manager_id_name);
        editText_id = (EditText) findViewById(R.id.edittext_manager_id_email);
        button_confirm = (Button) findViewById(R.id.button_manager_id_ok);
        button_back = (Button) findViewById(R.id.button_manager_id_back);

        textView_manager_name.setText(member.getName() + "점");
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean isPossible = true;
                if(!ValidateForm.checkForm(editText_id)){
                    isPossible = false;
                }

                if(!isPossible){
                    return;
                }
                PostString postString = new PostString();
                postString.setToken(member.getToken());
                postString.setID(editText_id.getText().toString());
                url = Post.URL + "/stamp/add";
                json = gson.toJson(postString);
                new AddTask().execute(url,json);
            }
        });

    }

    class AddTask extends PostTask{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;

            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);

                if (response.getStatus().equals("ok")) {
                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    this.cancel(true);
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

}


