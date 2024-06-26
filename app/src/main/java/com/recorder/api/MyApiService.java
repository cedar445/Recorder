package com.recorder.api;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.recorder.model.Bill;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONObject;

public class MyApiService extends AppCompatActivity {
    OkHttpClient client=new OkHttpClient();
    String conn="http://59.110.13.7/myapp/Account/";
    List<Bill>res;
    public List<Bill> get(String getUrl){
        Request request=new Request.Builder().url(conn+getUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("my", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json;
                        Gson gson=new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                .create();
                        ResponseBody responsebody=response.body();
                        try {
                            json=responsebody.string();
                            //Log.d("my", json);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Type listType = new TypeToken<List<Bill>>(){}.getType();
                        res = gson.fromJson(json, listType);

                    }
                });
            }
        });
        return res;
    }
    public void post(String postUrl,String type,String money,String remarks){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("money", money);
            jsonObject.put("cateID", 1);
            jsonObject.put("remarks", remarks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String json = jsonObject.toString();
        Log.d("my", json);
        RequestBody body = RequestBody.create(JSON,json);

        Request request=new Request.Builder()
                .url(conn+postUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("my", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ResponseBody responseBody=response.body();
                        try {
                            Log.d("my", response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }
}
