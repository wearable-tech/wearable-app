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

import org.wearableapp.bluetooth.BluetoothActivity;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        connectPulse(this);
        monitorContact(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void connectPulse(final Context context) {
        Button connectPulse = (Button) findViewById(R.id.connectPulse);
        connectPulse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("MONITOR_CONTACT", "Calling connect pulse");
                Intent intent = new Intent(context, BluetoothActivity.class);
                startActivity(intent);
            }
        });
    }

    private void monitorContact(final Context context) {
        Button monitorContact = (Button) findViewById(R.id.monitorContact);
        monitorContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("MONITOR_CONTACT", "Calling monitor contact");
                Intent intent = new Intent(context, ContactLevelActivity.class);
                startActivity(intent);
            }
        });
    }
}
