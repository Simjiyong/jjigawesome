package kr.co.company.jjigawesome;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

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
    public static final String API_URL = "http://openapi.seoul.go.kr:8088/49695a724d696e6f35384e52435059/json/SearchCulturalFacilitiesDetailService/1/1/";

    String url;
    String json;


    public Post(String url, String json){
        this.url = url;
        this.json = json;

    }
    public Post(String url){
        this.url = url;
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

    public String postAPI() throws IOException{
        Request request = new Request.Builder()
                .url(url)
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

class PostAPITask extends AsyncTask<String,String,String>{

    kr.co.company.jjigawesome.Response apiResponse;

    @Override
    protected String doInBackground(String... strings) {
        Post post = new Post(strings[0]);
        String response = null;
        try {
            response = post.postAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("PostAPITask", "postapitask!!");
        return response;
    }
}