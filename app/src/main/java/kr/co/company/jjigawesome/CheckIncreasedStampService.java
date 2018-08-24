package kr.co.company.jjigawesome;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class CheckIncreasedStampService extends Service {
    private static boolean isOn = false;
    private Thread checkIncreasedStampThread = null;
    private SharedPreferences mPrefs;
    private Member member;
    private Gson gson = new Gson();
    private int stampCount = 0;

    public CheckIncreasedStampService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);

        if(checkIncreasedStampThread == null){
            checkIncreasedStampThread = new Thread(){
                @Override
                public void run() {
                    isOn = true;
                    int time = 0;
                    mPrefs = getSharedPreferences("mPrefs", MODE_PRIVATE);
                    member=((Member)SPtoObject.loadObject(mPrefs,"member",Member.class));
                    stampCount = member.getStampCount();

                    PostString postString = new PostString();
                    postString.setToken(member.getToken());
                    String url = "http://18.218.187.138:3000/stamp/";
                    String json = gson.toJson(postString);
                    while(isOn) {
                        new CheckIncreasedStampService.GetStampTask().execute(url, json);
                        try{
                            Thread.sleep(1000);
                            time++;
                        }catch (InterruptedException e){
                            break;
                        }
                        if(time > 60){
                            this.interrupt();
                            checkIncreasedStampThread = null;
                        }
                    }
                }
            };
            checkIncreasedStampThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(checkIncreasedStampThread!=null){
            checkIncreasedStampThread.interrupt();
            checkIncreasedStampThread = null;
        }
    }

    private class GetStampTask extends PostTask{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("response", s);
                response = gson.fromJson(s, Response.class);
                if (response.getStatus().equals("ok")) {
                    if(stampCount < response.getNumber()){
                        isOn = false;
                        member.setStampCount(response.getNumber());
                        SPtoObject.saveObject(mPrefs,member,"member");
                        Intent intent = new Intent(CheckIncreasedStampService.this, CompleteQrcodeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        checkIncreasedStampThread.interrupt();
                        checkIncreasedStampThread = null;
                    }
                } else {
                    this.cancel(true);
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