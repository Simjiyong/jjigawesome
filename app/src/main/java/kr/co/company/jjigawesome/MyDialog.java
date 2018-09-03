package kr.co.company.jjigawesome;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by BHY on 2018. 7. 25..
 */

public class MyDialog extends Dialog {

    TextView textView;
    Button button_confirm;
    Button button_cancel;


    public MyDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        textView = (TextView) findViewById(R.id.textview_dialog);
        button_confirm = (Button) findViewById(R.id.button_dialog_confirm);
        button_cancel = (Button) findViewById(R.id.button_dialog_cancel);
    }

    public void showCancelButton(){
        button_cancel.setVisibility(View.VISIBLE);
    }

    public void setTextViewText(String string){
        textView.setText(string);
    }

    public Button getButton_confirm() {
        return button_confirm;
    }
    public Button getButton_cancel(){
        return button_cancel;
    }

}
