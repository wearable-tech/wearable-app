package org.wearableapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.wearableapp.users.LoginActivity;

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
            goToMenu();
        }
    };

    private void goToMenu() {
        Intent intent = new Intent(this, UserLevelActivity.class);
        startActivity(intent);
        finish();
    }
}
