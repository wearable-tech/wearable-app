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
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.wearableapp.MenuActivity;
import org.wearableapp.R;
import org.wearableapp.communications.HttpRequests;
import org.wearableapp.users.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class UserLevelActivity extends Activity {

    private Button notificationsLevel;
    private EditText notificationsLevelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_level);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        int level = sharedPreferences.getInt("level", 0);
        notificationsLevelText = (EditText) findViewById(R.id.edittext_notifications_level);
        notificationsLevelText.setText(String.valueOf(level));

        notificationsLevel = (Button) findViewById(R.id.button_notifications_level);
        notificationsLevel.setOnClickListener(onClickNotificationsLevel);

        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_level, menu);
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

    View.OnClickListener onClickNotificationsLevel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeLevel();
        }
    };

    private void goToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeLevel() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String level = notificationsLevelText.getText().toString();

        List params = new ArrayList();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("level", level));

        int response = HttpRequests.doPost(params, "/user/define_level");
        switch (response) {
            case 0:
                Log.i("CHANGE_LEVEL", "Level changed to user " + email);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("level", Integer.parseInt(level));
                editor.commit();
                goToMenu();
                break;
            case 1:
                Log.e("CHANGE_LEVEL", "Change Level failed");
                Toast.makeText(getApplicationContext(), "Erro ao mudar nível", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Log.e("CHANGE_LEVEL", "Can't connect to server");
                Toast.makeText(getApplicationContext(), "Não foi possível conectar ao servidor", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
