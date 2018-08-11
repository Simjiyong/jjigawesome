package kr.co.company.jjigawesome;

import android.os.AsyncTask;
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
    public static final String URL = "http://18.218.187.138:3000";

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
            return response.body().string();
        }
    }


}

class PostTask extends AsyncTask<String,String,String>{
    @Override
    protected String doInBackground(String... strings) {
        Post post = new Post(strings[0], strings[1]);
        String response = null;
        try {
            response = post.post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("PostTask", "posttask!!");
        return response;
    }
}