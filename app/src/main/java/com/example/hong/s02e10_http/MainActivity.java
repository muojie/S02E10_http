package com.example.hong.s02e10_http;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);

        ButtonListener buttonListener = new ButtonListener();
        button.setOnClickListener(buttonListener);
    }


    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Thread thread = new NetWorkThread();
            thread.start();
        }
    }

    class NetWorkThread extends Thread {
        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            //
            HttpGet httpGet = new HttpGet("http://www.marschen.com/data1.html");

            httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");

            Header[] reqHeader = httpGet.getAllHeaders();
            for(int i = 0; i < reqHeader.length; i++) {
                String name = reqHeader[i].getName();
                String value = reqHeader[i].getValue();

                Log.e("Http00", "name: " + name + "value: " + value);
            }

            try {
                HttpResponse response = httpClient.execute(httpGet);

                Header[] reqHeader1 = response.getAllHeaders();
                for(int i = 0; i < reqHeader1.length; i++) {
                    String name = reqHeader1[i].getName();
                    String value = reqHeader1[i].getValue();

                    Log.e("Http01", "name: " + name + "value: " + value);
                }

                int code = response.getStatusLine().getStatusCode();

                if(code == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream in = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = reader.readLine();

                    Log.d("Http", "从服务器取到的数据： " + line);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
