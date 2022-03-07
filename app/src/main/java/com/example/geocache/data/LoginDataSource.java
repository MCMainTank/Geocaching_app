package com.example.geocache.data;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.geocache.data.model.LoggedInUser;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {


    private OkHttpClient okHttpClient;
    private String responseString;
    private Integer status;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            new Thread(){
                @Override
                public void run(){
                    FormBody formBody = new FormBody.Builder()
                            .add("username", username).add("password", password).build();
                    RequestBody requestBody = RequestBody.create("{"+"\"username\":\""+username+"\",\"password\":\""+password+"\"}", MediaType.parse("application/json"));
                    Request request = new Request.Builder().url("http://10.0.2.2:8080/login")
                            .post(requestBody).build();
                    okHttpClient = new OkHttpClient();
                    Call call = okHttpClient.newCall(request);
//                    try {
//                        Response response = call.execute();
////                        if(response.body().string())
//                        Log.i(TAG,"postLoginSync: "+response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.i(TAG,"Login failed.");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            responseString = response.body().string();
                            Log.i(TAG,"postLoginAsync: "+responseString );

                        }
                    });
                }
            }.start();

            JSONObject jsonObject = new JSONObject(responseString);
            status = jsonObject.getInt("kstatus");
            if(status==1){
                LoggedInUser user =
                        new LoggedInUser(username,username);
                Log.i(TAG,"Successfully logged in.");
                return new Result.Success<>(user);
            }
            else return new Result.Error(new Exception("Wrong username password pairs."));


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
            return new Result.Error(new IOException("Error logging in, possibly network issue.", e));
        }
    }


    public void logout() {
        // TODO: revoke authentication
    }
}