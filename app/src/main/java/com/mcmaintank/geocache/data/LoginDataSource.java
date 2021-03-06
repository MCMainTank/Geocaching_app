package com.mcmaintank.geocache.data;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static java.lang.Thread.sleep;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mcmaintank.geocache.data.model.LoggedInUser;
import com.mcmaintank.geocache.data.model.UserInfoShp;

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


    private static final Object PREFS_NAME = "Login Status";
    private OkHttpClient okHttpClient;
    private String responseString;
    private Integer status;
    private static UserInfoShp userInfoShp;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            new Thread(){
                @Override
                public void run(){
                    FormBody formBody = new FormBody.Builder()
                            .add("username", username).add("password", password).build();
                    RequestBody requestBody = RequestBody.create("{"+"\"username\":\""+username+"\",\"password\":\""+password+"\"}", MediaType.parse("application/json"));
                    Request request = new Request.Builder().url("http://39.105.14.129:8080/login")
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
            sleep(700);
            JSONObject jsonObject = new JSONObject(responseString);
            status = jsonObject.getInt("kstatus");
            if(status==1){
                LoggedInUser user =
                        new LoggedInUser(username,username);
                Log.i(TAG,"Successfully logged in.");
//                saveLoginStates();
                return new Result.Success<>(user);
            }else if(status == 2){
                LoggedInUser user =
                        new LoggedInUser(username,username);
                Log.i(TAG,"Successfully logged in as admin.");
                return new Result.SuccessAsAdmin<>(user);
            }
            else return new Result.Error(new Exception("Wrong username password pairs."));


            // TODO: handle loggedInUser authentication
//            private void checkAuthenticity() {
//                try {
//                    //1.?????????---??????URL???????????????????????????
//                    URL url=new URL("http://localhost:8080/");
//                    //2.?????????---openCOnnection
//                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
//                    //3.?????????---InputStream
//                    InputStream inputStream= null;
//                    try {
//                        inputStream = httpURLConnection.getInputStream();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //4.????????????---InputStreamReader
//                    InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
//                    //5.??????????????????BufferedReader
//                    BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
//
//                    StringBuffer stringBuffer=new StringBuffer();
//                    String temp=null;
//
//                    //?????????????????????---while??????
//                    while ((temp=bufferedReader.readLine())!=null){
//                        stringBuffer.append(temp);
//                    }
//
//                    //???????????????????????????????????????
//                    bufferedReader.close();
//                    inputStreamReader.close();
//                    inputStream.close();
//                    //????????????
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

//    private void saveLoginStates(){
//        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = userInfo.edit();
//        editor.commit();
//        editor.putInt("status", 1);
//    }

}