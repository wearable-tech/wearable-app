package org.wearableapp.users;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wearableapp.App;
import org.wearableapp.MenuActivity;
import org.wearableapp.R;
import org.wearableapp.communications.HttpRequests;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    private EditText email;
    private EditText password;
    private CheckBox rememberMe;
    private Button newUser;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.edittext_email_login);
        password = (EditText) findViewById(R.id.edittext_password_login);
        rememberMe = (CheckBox) findViewById(R.id.checkbox_remember_me);
        newUser = (Button) findViewById(R.id.button_newuser_login);
        login = (Button) findViewById(R.id.button_login);

        login.setOnClickListener(onClickLogin);
        newUser.setOnClickListener(onClickNewUser);

        boolean remembered = App.getPreferences().getBoolean("remembered", false);

        if (remembered) {
            Log.i("LOGIN", "Logging in");
            goToMenu();
        }
    }

    View.OnClickListener onClickLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            login(email.getText().toString(), password.getText().toString());
        }
    };

    View.OnClickListener onClickNewUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            gotToNewUser();
        }
    };

    private void login(String email, String password) {
        Log.i("LOGIN", "Email: " + email);
        Log.i("LOGIN", "Password: " + password);

        List params = new ArrayList();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        int response = HttpRequests.doPost(params, "/user/get.json");
        switch (response) {
            case 0:
                Log.i("LOGIN", "Login success to user " + email);
                setPreferences();
                goToMenu();
                break;
            case 1:
                Log.e("LOGIN", "Login failed");
                Toast.makeText(getApplicationContext(), "Email e/ou senha inválidos", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Log.e("LOGIN", "Can't connect to server");
                Toast.makeText(getApplicationContext(), "Não foi possível conectar ao servidor", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void gotToNewUser() {
        Log.i("NEW_USER", "Calling new user");
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }

    private void goToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void setPreferences() {
        try {
            JSONArray jsonArray = new JSONArray(HttpRequests.getResponse());
            JSONObject object = jsonArray.getJSONObject(0);

            SharedPreferences.Editor editor = App.getPreferences().edit();

            if(rememberMe.isChecked()) {
                Log.i("PREFERENCES", "Saving remember-me");
                editor.putBoolean("remembered", true);
            }
            else {
                Log.i("PREFERENCES", "Removing remember-me");
                editor.putBoolean("remembered", false);
            }

            Log.i("PREFERENCES", "Name: " + object.getString("name") + " E-mail: " + object.getString("email") +
                    " Password: " + object.getString("password") + " Level: " + object.getInt("level"));

            editor.putString("name", object.getString("name"));
            editor.putString("email", object.getString("email"));
            editor.putString("password", object.getString("password"));
            editor.putInt("level", object.getInt("level"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
