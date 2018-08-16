package kr.co.company.jjigawesome;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Method;

public class QrScanActivity extends AppCompatActivity {

    IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        qrScan = new IntentIntegrator(QrScanActivity.this);
        qrScan.setBeepEnabled(false);
        qrScan.initiateScan();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.e("on", "onActivityResult" );

        if (result != null) {
            if (result.getContents() == null) {
                Log.d("qrcode :::::::::::", "no contents");
            } else { //QR코드, 내용 존재
                try {
                    /* QR 코드 내용*/
                    String temp = result.getContents();
                    Log.d("qrcode Contents :", temp);
                    Intent intent = new Intent(QrScanActivity.this, ManagerQrActivity.class);
                    intent.putExtra("data", temp);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Exception :", "QR code fail");

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }
}
