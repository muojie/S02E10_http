package com.example.hong.s02e10_http;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hong.s02e10_http.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/11/28.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        Button sendRequest2 = (Button) findViewById(R.id.send_request_connection);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
        sendRequest2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
//            String address = "http://www.w3school.com.cn/example/xmle/simple.xml";
            String address = "http://saas-rel.haimawan.com:8081/s/rest/api";
            HttpUtil.sendRequestWithOkHttp(address, new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    showResponse(response.toString());
//                    HttpUtil.parseXMLWithPull(responseData);
//                    HttpUtil.parseXMLWithSAX(responseData);
                    HttpUtil.parseJSONWithJSONObject(responseData);
                    HttpUtil.paseJSONWithGSON(responseData);
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
        } else if (v.getId() == R.id.send_request_connection){
            String address = "https://www.baidu.com";

            HttpUtil.sendRequestWithHttpURLConnection(address, new HttpUtil.HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    showResponse(response);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在这里进行UI操作，将结果显示在界面上
                responseText.setText(response);
            }
        });
    }
}
