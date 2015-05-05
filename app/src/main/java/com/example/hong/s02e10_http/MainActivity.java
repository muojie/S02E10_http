package com.example.hong.s02e10_http;

import android.os.Environment;
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
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
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
    private Button submit2;

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

        submit2 = (Button)findViewById(R.id.submit2);
        submit2.setOnClickListener(buttonListener1);
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
                Log.d("onClick", "submit");
                GetThread gt = new GetThread(name, pwd);
                gt.start();
            } else if(v.getId() == R.id.submit1) {
                Log.d("onClick", "submit1");
                PostThread pt = new PostThread(name, pwd);
                pt.start();
            } else if(v.getId() == R.id.submit2) {
                Log.d("onClick", "submit2");
                UploadThread ut = new UploadThread();
                ut.start();
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

    // for S02E13
    class UploadThread extends Thread {
        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            //上传文件只能使用POST方法。上传文件的URL由服务器开发人员规定
            HttpPost httpPost = new HttpPost("http://www.baidu.com");
            String filePath = Environment.getDataDirectory().toString() + File.separator + "system/packages.xml";
            Log.d("UploadThread", "file path: " + filePath);
            File file = new File(filePath);
            FileBody fileBody = new FileBody(file);
            //生成一个ContentType对象，该对象用于表示数据的类型，
            //Create方法第一个参数用于表示数据的类型，第二个参数用于指定数据所使用的字符集
            ContentType contentType = ContentType.create("text/html", "GBK");
            //生成一个代表字符串请求内容的对象
            StringBody strBody = new StringBody("ZhangSan", contentType);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("uploadfile", fileBody);
            builder.addPart("testdata", strBody);

            //通过builder创建一个请求对象
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);

            try {
                Log.e("Upload", "to execute http post");
                HttpResponse response = httpClient.execute(httpPost);
                if(response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity en = response.getEntity();
                    //在读取服务器端的响应时，需要跟服务器开发人员确认字符编码
                    BufferedReader reader = new BufferedReader(new InputStreamReader(en.getContent(), "GBK"));
                    String result = reader.readLine();
                    Log.d("Upload", "result: " + result);
                }
            } catch (Exception e) {
                Log.e("Upload", "no response");
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
