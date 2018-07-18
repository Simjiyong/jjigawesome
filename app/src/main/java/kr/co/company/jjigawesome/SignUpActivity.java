package kr.co.company.jjigawesome;

import android.content.Context;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SignUpActivity extends AppCompatActivity {

    int newUiOptions;
    View view;

    LinearLayout linearLayout;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        view = getWindow().getDecorView();
        int uiOptions = view.getSystemUiVisibility();
        newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
// 몰입 모드를 꼭 적용해야 한다면 아래의 3가지 속성을 모두 적용시켜야 합니다
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

        //linearLayout = (LinearLayout) findViewById(R.id.linear_signup);
        //linearLayout.setPadding(0,getStatusBarHeight(),0,0);
        Log.d("status" ,"status : " + getStatusBarHeight());


        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mgr = (WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        mgr.getDefaultDisplay().getMetrics(metrics);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Log.d("TAG", "densityDPI = " + metrics.densityDpi + "  " + height);

        editText = (EditText) findViewById(R.id.edit_signup_name);

        LinearLayout.MarginLayoutParams marginLayoutParams = new LinearLayout.MarginLayoutParams(0,0);
        marginLayoutParams.setMargins(0,(int)((113*((metrics.densityDpi/160))*((float)height/1920))),0,0);
        editText.setLayoutParams(new LinearLayout.LayoutParams(marginLayoutParams));

        Log.d("ddd", ""+(int)((113*((metrics.densityDpi/160))*(float)height/1920)));



    }

    public int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);

        return result;
    }
}
