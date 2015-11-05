package org.wearableapp.users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.wearableapp.App;
import org.wearableapp.MenuActivity;
import org.wearableapp.R;
import org.wearableapp.communications.HttpRequests;
import org.wearableapp.services.LocationService;
import org.wearableapp.services.MqttService;
import org.wearableapp.services.SubscribeService;

import java.util.ArrayList;
import java.util.List;

public class UserAccountActivity extends Activity {
    private EditText etName;
    private EditText etEmail;
    private EditText etLevel;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String name = App.getPreferences().getString("name", "");
        String email = App.getPreferences().getString("email", "");
        Integer level = App.getPreferences().getInt("level", 0);

        etName = (EditText) findViewById(R.id.name);
        etEmail= (EditText) findViewById(R.id.email);
        etLevel = (EditText) findViewById(R.id.level);
        etCurrentPassword = (EditText) findViewById(R.id.currentPassword);
        etNewPassword = (EditText) findViewById(R.id.newPassword);

        Button save = (Button) findViewById(R.id.updateUser);
        save.setOnClickListener(onClickUpdateUser);

        etName.setText(name);
        etEmail.setText(email);
        etLevel.setText(level.toString());

        currentEmail = email;

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
        getMenuInflater().inflate(R.menu.menu_delete_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_delete_account) {
            deleteAccount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onClickUpdateUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("USER", "Updating user");
            updateUser();
        }
    };

    private void updateUser() {
        List params = new ArrayList();
        params.add(etName);
        params.add(etEmail);
        params.add(etCurrentPassword);
        params.add(etNewPassword);

        if (!validateUser((ArrayList<EditText>) params)) {
            Toast.makeText(getApplicationContext(), "Cheque os campos", Toast.LENGTH_LONG).show();
            return;
        }

        params.clear();

        params.add(new BasicNameValuePair("name", etName.getText().toString().trim()));
        params.add(new BasicNameValuePair("current_email", currentEmail));
        params.add(new BasicNameValuePair("new_email", etEmail.getText().toString().trim()));
        params.add(new BasicNameValuePair("level", etLevel.getText().toString()));
        if (etCurrentPassword.getText().length() > 0) {
            params.add(new BasicNameValuePair("password", etNewPassword.getText().toString()));
        }

        BasicNameValuePair email = (BasicNameValuePair) params.get(2);

        if (HttpRequests.doPost(params, "/user/update") == 0) {
            Log.i("UPDATE_USER", "Success to update user: " + email.getValue());
            Toast.makeText(getApplicationContext(), "Dados alterados com sucesso", Toast.LENGTH_LONG).show();
            updatePreferences((List<BasicNameValuePair>) params);
            goToMenu();
        } else {
            Log.i("UPDATE_USER", "Failed to update user: " + email.getValue());
            Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
        }
    }

    private void updatePreferences(List<BasicNameValuePair> params) {
        SharedPreferences.Editor editor = App.getPreferences().edit();
        editor.putString("name", params.get(0).getValue());
        editor.putString("email", params.get(2).getValue());
        editor.putInt("level", Integer.parseInt(params.get(3).getValue()));

        if (params.size() > 4) {
            editor.putString("password", params.get(4).getValue());
        }

        editor.commit();
    }

    private boolean validateUser(ArrayList<EditText> user) {
        if (!UserValidation.name(user.get(0))) return false;
        if (!UserValidation.email(user.get(1))) return false;
        if (user.get(2).getText().length() == 0) return true;
        if (!UserValidation.comparePasswords(user.get(2), user.get(3))) return false;

        return true;
    }

    private void goToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }


    private void deleteAccount() {
        List params = new ArrayList();
        params.add(new BasicNameValuePair("email_user", currentEmail));

        if (HttpRequests.doPost(params, "/user/delete") == 0) {
            Toast.makeText(getApplicationContext(), "Usuário apagado", Toast.LENGTH_LONG).show();
            logout();
        } else {
            Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = App.getPreferences().edit();

        editor.putBoolean("remembered", false);
        editor.remove("email");
        editor.remove("notifications");
        editor.commit();

        stopServices();
        goToLogin();
    }


    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void stopServices() {
        Intent intent;

        intent = new Intent(this, MqttService.class);
        stopService(intent);
        Log.i("SUBSCRIBE_SERVICE", "Subscribe service destroyed!");

        intent = new Intent(this, SubscribeService.class);
        stopService(intent);
        Log.i("STOP_SERVICES", "MQTT Service Destroyed!!!");

        intent = new Intent(this, LocationService.class);
        stopService(intent);
        Log.i("STOP_SERVICES", "Location Service Destroyed!!!");
    }
}
