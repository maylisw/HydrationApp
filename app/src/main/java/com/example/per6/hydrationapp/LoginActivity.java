package com.example.per6.hydrationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button submitButton;
    private EditText usernameEdittext, passwordEdittext;
    private SharedPreferences sharedPref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        wireWidgets();

    }

    private void wireWidgets() {
        submitButton=(Button)findViewById(R.id.button_submit);
        usernameEdittext=(EditText) findViewById(R.id.login_edittext_username);
        passwordEdittext=(EditText) findViewById(R.id.login_edittext_password);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.UserService.login(usernameEdittext.getText().toString(), passwordEdittext.getText().toString(), new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        String username=(String) response.getProperty("username");
                        String password=response.getPassword();
                        Toast.makeText(LoginActivity.this, "Hello " +username, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.user_ID), response.getUserId());
                        editor.putString("userUserName", username);
                        editor.putString("userPassword", password);
                        editor.putInt(getString(R.string.user), 1);
                        editor.commit();
                }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.d(TAG, "handleFault: "+fault.getCode());
                    }

            });
        }

    });


    }
}