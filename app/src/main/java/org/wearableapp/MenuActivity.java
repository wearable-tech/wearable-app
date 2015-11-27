package org.wearableapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import org.wearableapp.users.UserAccountActivity;

public class MenuActivity extends FragmentActivity {

    private CompoundButton activateNotifications;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean("logout")) {
                logout();
            }
        }

        String email = App.getPreferences().getString("email", "");
        Log.i("USER_CONNECTED", "Email: " + email);

        activateNotifications = (CompoundButton) findViewById(R.id.switch_activate_notifications);
        activateNotifications.setOnClickListener(onClickActivateNotifications);

        initServices();

        boolean notifications = App.getPreferences().getBoolean("notifications", false);
        if (notifications) {
            activateNotifications.setChecked(true);
        }

        welcome = (TextView) findViewById(R.id.welcome_textView);
        welcome.setText("Bem Vindo " + App.getPreferences().getString("name", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (App.getPreferences().getBoolean("wearableStatus", false)) {
            Log.i("MENU_ACTIVITY", "Load graph!");
            welcome.setVisibility(View.INVISIBLE);
            loadGraph();
        }
        else {
            welcome.setVisibility(View.VISIBLE);
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
            goToUserAccount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onClickActivateNotifications = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("NOTIFICATIONS", "Notifications: " + activateNotifications.isChecked());
            subscribeServiceController(activateNotifications.isChecked());

            SharedPreferences.Editor editor = App.getPreferences().edit();
            editor.putBoolean("notifications", activateNotifications.isChecked());
            editor.commit();
        }
    };

    private void loadGraph() {
        Fragment fragment = new Graph();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void goToBluetooth() {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    private void goToContatctList() {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }

    private void goToUserAccount() {
        Intent intent = new Intent(this, UserAccountActivity.class);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences.Editor editor = App.getPreferences().edit();

        editor.putBoolean("remembered", false);
        editor.remove("email");
        editor.remove("notifications");
        editor.commit();

        stopServices();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initServices() {
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
        stopService(intent);
        Log.i("STOP_SERVICES", "MQTT Service Destroyed!!!");

        intent = new Intent(this, LocationService.class);
        stopService(intent);
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
