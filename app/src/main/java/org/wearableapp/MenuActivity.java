package org.wearableapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.wearableapp.bluetooth.BluetoothActivity;

public class MenuActivity extends Activity {

    private Button monitorContact;
    private Button connectBracelet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        monitorContact = (Button) findViewById(R.id.button_monitor_contact);
        connectBracelet = (Button) findViewById(R.id.button_connect_bracelet);

        monitorContact.setOnClickListener(onClickMonitorContact);
        connectBracelet.setOnClickListener(onClickConnectBracelet);
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
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onClickMonitorContact = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Log.i("MONITOR_CONTACT", "Calling connect pulse");
            goToContatctLevel();
        }
    };

    View.OnClickListener onClickConnectBracelet = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Log.i("MONITOR_CONTACT", "Calling connect pulse");
            goToBluetooth();
        }
    };

    private void goToBluetooth() {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    private void goToContatctLevel() {
        Intent intent = new Intent(this, ContactLevelActivity.class);
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
