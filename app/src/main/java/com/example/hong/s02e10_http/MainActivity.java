package com.example.hong.s02e10_http;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private Button button;

    private EditText nameText;
    private EditText pwdText;
    private Button submit;
    private Button submit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);

        ButtonListener buttonListener = new ButtonListener();
        button.setOnClickListener(buttonListener);

        // for S02E12
        nameText = (EditText)findViewById(R.id.username);
        pwdText = (EditText)findViewById(R.id.passwd);
        submit = (Button)findViewById(R.id.submit);
        submit1 = (Button)findViewById(R.id.submit1);
        ButtonListener1 buttonListener1 = new ButtonListener1();
        submit.setOnClickListener(buttonListener1);
        submit1.setOnClickListener(buttonListener1);
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

    // for S02E12
    class ButtonListener1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String name = nameText.getText().toString();
            String pwd = pwdText.getText().toString();

            if(v.getId() == R.id.submit) {
                GetThread gt = new GetThread(name, pwd);
                gt.start();
            } else if(v.getId() == R.id.submit1) {
                PostThread pt = new PostThread(name, pwd);
                pt.start();
            }

        }
    }

    class GetThread extends Thread {

        String name;
        String pwd;

        public GetThread(String name, String pwd) {
            this.name = name;
            this.pwd = pwd;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://www.baidu.com";
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String result = reader.readLine();
                    Log.d("HTTP", "result(Get): " + result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class PostThread extends Thread {
        String name;
        String pwd;

        public PostThread(String name, String pwd) {
            this.name = name;
            this.pwd = pwd;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://www.baidu.com";
            //生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的健值对
            NameValuePair pair1 = new BasicNameValuePair("name", name);
            NameValuePair pari2 = new BasicNameValuePair("password", pwd);
            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(pair1);
            pairs.add(pari2);

            try {
                //创建代表请求体的对象
                HttpEntity requestEntiy = new UrlEncodedFormEntity(pairs);
                //将请求放置在请求对象当中
                httpPost.setEntity(requestEntiy);
                //执行请求对象
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    if(response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.e("HTTP", "result(Post): " + result);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
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
