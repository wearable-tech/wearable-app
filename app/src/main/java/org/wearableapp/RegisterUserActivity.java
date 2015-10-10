package org.wearableapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.wearableapp.communications.HttpRequests;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegisterUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        saveUser(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_user, menu);
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

    private void saveUser(final Context context) {
        Button save = (Button) findViewById(R.id.saveUser);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.emailUser);
                EditText password = (EditText) findViewById(R.id.passwordUser);

                Log.i("USER_SAVE", "Email: " + email.getText().toString());
                Log.i("USER_SAVE", "Password: " + password.getText().toString());

                List params = new ArrayList();
                params.add(new BasicNameValuePair("email", email.getText().toString()));
                params.add(new BasicNameValuePair("password", password.getText().toString()));

                if (doPost(params)) {
                    Log.i("CREATE_USER", "Create user success");
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Log.e("CREATE_USER", "Create user error");
                    Toast.makeText(getApplicationContext(), "Erro na conexão, tente novamente!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean doPost(final List params) {
        AsyncTask<Void, Void, String> postTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                HttpResponse response = null;
                try {
                    response = HttpRequests.doPost(params, "/user/save");
                } catch (URISyntaxException e) {
                    Log.i("ERROR", "Connection refused 1");
                    e.printStackTrace();
                }

                if (response != null) {
                    return "success";
                }
                return "fail";
            }
        };
        postTask.execute();

        try {
            return postTask.get() == "success";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }
}
