package kr.co.company.jjigawesome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChangeNameActivity extends AppCompatActivity {

    Button button_qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);


        button_qrcode = (Button) findViewById(R.id.button_buystamp_qrcode);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.button_buystamp_qrcode:
                        Intent intent = new Intent(ChangeNameActivity.this,  QrcodeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                }
            }
        };

        button_qrcode.setOnClickListener(onClickListener);
    }
}
