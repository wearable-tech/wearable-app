package org.wearableapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.wearableapp.bluetooth.BluetoothActivity;
import org.wearableapp.services.LocationService;
import org.wearableapp.services.MqttService;
import org.wearableapp.services.SubscribeService;
import org.wearableapp.users.ContactListActivity;
import org.wearableapp.users.LoginActivity;
import org.wearableapp.users.UserLevelActivity;

public class MenuActivity extends Activity {

    private CompoundButton activateNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView greetingTextView = (TextView) findViewById(R.id.textview_greeting);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String name = sharedPreferences.getString("name", "");
        Log.i("USER_CONNECTED", "Email is: " + email);

        if (!email.isEmpty()) {
            greetingTextView.setText("Olá, " + name + "!");
        }

        activateNotifications = (CompoundButton) findViewById(R.id.switch_activate_notifications);
        activateNotifications.setOnClickListener(onClickActivateNotifications);

        iniServices();

        boolean notifications = sharedPreferences.getBoolean("notifications", false);
        if (notifications) {
            activateNotifications.setChecked(true);
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
        }
        else if (id == R.id.menu_connect_wearable) {
            goToBluetooth();
            return true;
        }
        else if (id == R.id.menu_monitor_contact) {
            goToContatctList();
            return true;
        }
        else if (id == R.id.menu_define_user_level) {
            goToUserLevel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onClickActivateNotifications = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Log.i("NOFICATIONS", "Notifications are " + activateNotifications.isChecked());
        subscribeServiceController(activateNotifications.isChecked());

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", activateNotifications.isChecked());
        editor.commit();
        }
    };

    private void goToBluetooth() {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    private void goToContatctList() {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }

    private void goToUserLevel() {
        Intent intent = new Intent(this, UserLevelActivity.class);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("remembered", false);
        editor.remove("email");
        editor.remove("notifications");
        editor.commit();

        stopServices();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void iniServices() {
        Intent intent;

        intent = new Intent(this, MqttService.class);
        startService(intent);
        Log.i("INIT_SERVICES", "MQTT Service Created!!!");

        intent = new Intent(this, LocationService.class);
        startService(intent);
        Log.i("INIT_SERVICES", "Location Service Created!!!");
    }

    private void stopServices() {
        subscribeServiceController(false);

        Intent intent;

        intent = new Intent(this, MqttService.class);
        startService(intent);
        Log.i("STOP_SERVICES", "MQTT Service Destroyed!!!");

        intent = new Intent(this, LocationService.class);
        startService(intent);
        Log.i("STOP_SERVICES", "Location Service Destroyed!!!");
    }

    private void subscribeServiceController(boolean type) {
        Intent intent = new Intent(this, SubscribeService.class);

        if (type) {
            startService(intent);
            Log.i("SUBSCRIBE_SERVICE", "Subscribe service started!");
        }
        else {
            stopService(intent);
            Log.i("SUBSCRIBE_SERVICE", "Subscribe service destroyed!");
        }
    }
}
