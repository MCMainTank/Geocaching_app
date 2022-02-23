package com.example.geocache.data;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import com.example.geocache.data.model.LoggedInUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {


    private OkHttpClient okHttpClient;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            new Thread(){
                @Override
                public void run(){
                    FormBody formBody = new FormBody.Builder()
                            .add("username", username).add("password", password).build();
                    Request request = new Request.Builder().url("http://192.108.1.9/")
                            .post(formBody).build();
                    Call call = okHttpClient.newCall(request);
                    try {
                        Response response = call.execute();
//                        if(response.body().string())
                        Log.i(TAG,"postLoginSync: "+response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            LoggedInUser user =
                    new LoggedInUser(
                            username,username);
            return new Result.Success<>(user);
            // TODO: handle loggedInUser authentication
//            private void checkAuthenticity() {
//                try {
//                    //1.找水源---创建URL（统一资源定位器）
//                    URL url=new URL("http://localhost:8080/");
//                    //2.开水闸---openCOnnection
//                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
//                    //3.建管道---InputStream
//                    InputStream inputStream= null;
//                    try {
//                        inputStream = httpURLConnection.getInputStream();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //4.建蓄水池---InputStreamReader
//                    InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
//                    //5.水桶盛水——BufferedReader
//                    BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
//
//                    StringBuffer stringBuffer=new StringBuffer();
//                    String temp=null;
//
//                    //循环做盛水工作---while循环
//                    while ((temp=bufferedReader.readLine())!=null){
//                        stringBuffer.append(temp);
//                    }
//
//                    //关闭水池入口，从管道到水桶
//                    bufferedReader.close();
//                    inputStreamReader.close();
//                    inputStream.close();
//                    //打印日志
//                    Log.e("Main",stringBuffer.toString());
//
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }





        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }


    public void logout() {
        // TODO: revoke authentication
    }
}