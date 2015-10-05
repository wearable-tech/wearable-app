package org.wearableapp.bluetooth;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.wearableapp.MainActivity;
import org.wearableapp.R;

public class BluetoothActivity extends Activity {
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private TextView mText;
    private BluetoothAdapter mBluetoothAdapter;
    private Button mlistBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = (TextView) findViewById(R.id.text);

        Button onBtn = (Button) findViewById(R.id.turnOn);
        onBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                on(v);
            }
        });

        Button offBtn = (Button) findViewById(R.id.turnOff);
        offBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                off(v);
            }
        });

        mlistBtn = (Button) findViewById(R.id.paired);
        mlistBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isEnabled()) {
                    list(v);
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth was turned off",
                            Toast.LENGTH_LONG).show();
                    mlistBtn.setEnabled(false);
                    mText.setText("Status: Disabled");
                }
            }
        });

        // take an instance of BluetoothAdapter - Bluetooth radio
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            mlistBtn.setEnabled(false);
            mText.setText("Status: BT not supported");

            Toast.makeText(getApplicationContext(), "Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
        } else if (mBluetoothAdapter.isEnabled()) {
            mText.setText("Status: Enabled");
        } else {
            mlistBtn.setEnabled(false);
            mText.setText("Status: Disabled");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Bluetooth turned on" ,
                        Toast.LENGTH_LONG).show();
                mlistBtn.setEnabled(true);
                mText.setText("Status: Enabled");
            } else {
                mText.setText("Status: Disabled");
            }
        }
    }

    public void on(View view) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already on",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void off(View view) {
        mBluetoothAdapter.disable();
        mText.setText("Status: Disabled");

        Toast.makeText(getApplicationContext(), "Bluetooth turned off",
                Toast.LENGTH_LONG).show();
    }

    public void list(View view) {
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar to open MQTT connection
        if (item.getItemId() == R.id.mqtt_connection) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}