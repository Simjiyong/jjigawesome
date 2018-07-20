package kr.co.company.jjigawesome;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by BHY on 2018. 7. 20..
 */

public class Post {
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String url;
    String json;

    public Post(String url, String json){
        this.url = url;
        this.json = json;

    }

    public String post() throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("accept","application/x-www-form-urlencoded")
                .url(url)
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()){
            Log.d("response" , "response : " + response.body().string());
            return response.body().string();
        }
    }
}
