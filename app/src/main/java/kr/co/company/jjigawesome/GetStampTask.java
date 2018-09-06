package kr.co.company.jjigawesome;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by BHY on 2018. 8. 31..
 */

public class GetStampTask extends PostTask {
    Member member;
    String url;
    String json;
    Gson gson = new Gson();
    SharedPreferences mPrefs;
    Context context;

    public GetStampTask(Context context,SharedPreferences mPrefs, Member member) {
        this.context = context;
        this.mPrefs = mPrefs;
        this.member = member;
        PostString postString = new PostString();
        postString.setToken(member.getToken());
        url = "http://18.218.187.138:3000/stamp/";
        json = gson.toJson(postString);
    }

    @Override
    protected String doInBackground(String... strings) {
        return super.doInBackground(url, json);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Response response;
        try {
            Log.d("response", s);
            response = gson.fromJson(s, Response.class);

            if (response.getStatus().equals("ok")) {
                member.setStampCount(response.getNumber());
                SPtoObject.saveObject(mPrefs,member,"member");
            } else {
                this.cancel(true);
            }
        } catch (NullPointerException e){
            Toast.makeText(context, "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(context, "오류!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
