package com.mcmaintank.geocache.ui.login;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.UserInfoShp;
import com.mcmaintank.geocache.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LoginStatus";
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private static UserInfoShp userInfoShp;
    private OkHttpClient okHttpClient;
    private String responseString;
    private Integer status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;



        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                    Log.e(TAG, "Failed...");
                }
                if (loginResult.getSuccess() != null&&loginViewModel.getSuccess()==1) {
                    //Jump to create and get geocache activity.
                    Log.e(TAG, "Success!");
                    SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = userInfo.edit();
                    userInfoShp.setUserName(LoginActivity.this,usernameEditText.getText().toString());
                    userInfoShp.setUserPassword(LoginActivity.this, passwordEditText.getText().toString());
                    editor.commit();
                    editor.putInt("status", 1);
                    updateUiWithUser(loginResult.getSuccess());
                    if(loginViewModel.getLoggedInUserGroup()==0){
                        startActivity(new Intent(LoginActivity.this,ServiceSelectionActivity.class));
                    }
                    else if(loginViewModel.getLoggedInUserGroup()==1){
                        startActivity(new Intent(LoginActivity.this,AdminServiceSelectionActivity.class));
                    }
                }else{
                    return;
                }
                setResult(Activity.RESULT_OK);
                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        RequestBody requestBody = RequestBody.create("{" + "\"username\":\"" + usernameEditText.getText().toString() + "\",\"password\":\"" + passwordEditText.getText().toString() + "\"}", MediaType.parse("application/json"));
                        Request request = new Request.Builder().url("http://10.0.2.2:8080/register")
                                .post(requestBody).build();
                        okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.i(TAG, "Login failed.");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                responseString = response.body().string();
                                Log.i(TAG, "postLoginAsync: " + responseString);
                            }
                        });
//                        Response response = null;
//                        try {
//                            response = call.execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            responseString = response.body().string();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                    }
                }.start();
                try {
                    sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    status = jsonObject.getInt("kstatus");
                    if (status == 1) {
                        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = userInfo.edit();
                        userInfoShp.setUserName(LoginActivity.this, usernameEditText.getText().toString());
                        userInfoShp.setUserPassword(LoginActivity.this, passwordEditText.getText().toString());
                        editor.putInt("status", 1);
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this,ServiceSelectionActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Register failed, user name taken.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        startActivity(new Intent(LoginActivity.this,ServiceSelectionActivity.class));
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}