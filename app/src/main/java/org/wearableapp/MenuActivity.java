package org.wearableapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.wearableapp.bluetooth.BluetoothActivity;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView greetingTextView = (TextView) findViewById(R.id.textview_greeting);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Log.i("USER_CONNECTED", "Email is: " + email);
        if (!email.isEmpty()) {
            greetingTextView.setText("Olá, " + email);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_logout) {
            logout();
            return true;
        } else if (id == R.id.menu_connect_wearable) {
            goToBluetooth();
            return true;
        } else if (id == R.id.menu_monitor_contact) {
            goToContatctList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToBluetooth() {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    private void goToContatctList() {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("remembered", false);
        editor.remove("email");
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
