package org.wearableapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.wearableapp.communications.HttpRequests;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newUser(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private void getUser(final Context context) {
        Button save = (Button) findViewById(R.id.saveUser);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.emailLogin);
                EditText password = (EditText) findViewById(R.id.passwordLogin);

                Log.i("USER_GET", "Email: " + email.getText().toString());
                Log.i("USER_GET", "Password: " + password.getText().toString());

                List params = new ArrayList();
                params.add(new BasicNameValuePair("email", email.getText().toString()));
                params.add(new BasicNameValuePair("password", password.getText().toString()));

                if (HttpRequests.doPost(params, "/user/get")) {
                    Log.i("CREATE_USER", "Create user success");
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado!", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.e("CREATE_USER", "Create user error");
                    Toast.makeText(getApplicationContext(), "Erro, tente novamente!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void newUser(final Context context) {
        Button save = (Button) findViewById(R.id.newUser);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("NEW_USER", "Calling new user");
                Intent intent = new Intent(context, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
    }
}
