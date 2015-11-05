package org.wearableapp.users;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.wearableapp.App;
import org.wearableapp.MenuActivity;
import org.wearableapp.R;

public class UserAccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String name = App.getPreferences().getString("name", "");
        String email = App.getPreferences().getString("email", "");
        Integer level = App.getPreferences().getInt("level", 0);

        EditText etName = (EditText) findViewById(R.id.name);
        EditText etEmail= (EditText) findViewById(R.id.email);
        EditText etLevel = (EditText) findViewById(R.id.level);
        EditText etCurrentPassword = (EditText) findViewById(R.id.currentPassword);
        EditText etNewPassword = (EditText) findViewById(R.id.newPassword);

        etName.setText(name);
        etEmail.setText(email);
        etLevel.setText(level.toString());

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
        return super.onOptionsItemSelected(item);
    }
}
