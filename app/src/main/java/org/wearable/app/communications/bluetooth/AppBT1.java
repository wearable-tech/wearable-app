package org.wearable.app.communications.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.wearable.app.R;

public class AppBT1 extends Activity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PAIRED_DEVICE = 2;
    /** Called when the activity is first created. */

    Button btnListPairedDevices;
    TextView stateBluetooth;
    BluetoothAdapter bluetoothAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG", "Hello");
        setContentView(R.layout.activity_main);
        btnListPairedDevices = (Button) findViewById(R.id.listpaireddevices);
        stateBluetooth = (TextView) findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        CheckBluetoothState();
        btnListPairedDevices.setOnClickListener(btnListPairedDevicesOnClickListener);
    }

    private void CheckBluetoothState() {

        if (bluetoothAdapter == null) {
            stateBluetooth.setText("Bluetooth NOT supported!");
        } else if (bluetoothAdapter.isEnabled()) {
            if (bluetoothAdapter.isDiscovering()) {
                stateBluetooth.setText("Bluetooth is currently in device discovery process.");
            } else {
                stateBluetooth.setText("Bluetooth is Enabled.");
                btnListPairedDevices.setEnabled(true);
            }
        } else {
            stateBluetooth.setText("Bluetooth is NOT Enabled!");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    private Button.OnClickListener btnListPairedDevicesOnClickListener
            = new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent();
            intent.setClass(AppBT1.this, DeviceListActivity.class);
            startActivityForResult(intent, REQUEST_PAIRED_DEVICE);
        }};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBluetoothState();
        }

        if (requestCode == REQUEST_PAIRED_DEVICE) {
            if (resultCode == RESULT_OK) {
                // Do something
            }
        }
    }
}
