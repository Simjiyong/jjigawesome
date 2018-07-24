package kr.co.company.jjigawesome;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;

/**
 * Created by BHY on 2018. 7. 25..
 */

public class MyDialog extends Dialog {
    public MyDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
    }
}
