package org.wearableapp.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.wearableapp.R;

import java.util.Set;

public class BluetoothTestActivity extends Activity {

    private static final String BLUETOOTH_NAME = "HC-05";
    private static final int REQUEST_ENABLE_BT = 2;
    private CompoundButton activeBluetooth;
    private CompoundButton activeBracelet;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice braceletDevice;
    private BluetoothConnector bluetoothConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        isBluetoothSupported();

        activeBluetooth = (CompoundButton) findViewById(R.id.switch_activate_bluetooth);
        activeBluetooth.setOnClickListener(onClickActiveBluetooth);
        activeBluetooth.setChecked(bluetoothAdapter.isEnabled());

        activeBracelet = (CompoundButton) findViewById(R.id.switch_activate_bracelet);
        activeBracelet.setOnClickListener(onClickActiveBracelet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onClickActiveBluetooth = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (activeBluetooth.isChecked()) {
                activeBluetooth();
            } else {
                disableBluetooth();
            }
        }
    };

    private boolean isBluetoothSupported() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Dispositivo não suporta bluetooth!", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        return true;
    }

    private void activeBluetooth() {
        if (!bluetoothAdapter.isEnabled() && isBluetoothSupported()) {
            Log.i("ACTIVE_BLUETOOTH", "Starting bluetooth");
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        }
    }

    private void disableBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            activeBracelet.setChecked(false);
        }
    }

    View.OnClickListener onClickActiveBracelet = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (activeBracelet.isChecked()) {

                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    getBraceletDevice();

                    if (braceletDevice != null) {
                        Log.i("BLUETOOTH_CONNECTOR", "Calling thread to connect bluetooth");
                        bluetoothConnector = new BluetoothConnector(braceletDevice, bluetoothAdapter);
                        bluetoothConnector.start();
                    } else {
                        Toast.makeText(getApplicationContext(), "Pulseira não encontrada nos dispositivos pareados", Toast.LENGTH_LONG).show();
                    }
                } else {
                    activeBracelet.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Bluetooth desligado", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            activeBluetooth.setChecked(bluetoothAdapter.isEnabled());
        }
    }

    private void getBraceletDevice() {
        braceletDevice = getPairedDevices();

        if (braceletDevice == null) {
            getOtherDevices();
        }
    }

    private BluetoothDevice getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        Log.i("PAIRED_DEVICES", "Paired devices found " + pairedDevices.size());
        for (BluetoothDevice device : pairedDevices) {
            Log.i("DEVICE_PAIRED", "Name: " + device.getName() + " Address: " + device.getAddress());

            if (device.getName().contains(BLUETOOTH_NAME)) {
                return device;
            }
        }

        return null;
    }

    private void getOtherDevices() {
        bluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("DEVICE_FOND", "Name: " + device.getName() + " Address: " + device.getAddress());

                if (device.getName() == BLUETOOTH_NAME) {
                    braceletDevice = device;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mReceiver);
            bluetoothConnector.cancel();
        } catch (Exception ignored) {}

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        super.onDestroy();
    }
}
