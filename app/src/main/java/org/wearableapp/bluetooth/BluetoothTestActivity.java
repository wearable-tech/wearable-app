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
import org.wearableapp.bluetooth.BluetoothConnector;

import java.util.Set;

public class BluetoothTestActivity extends Activity {

    private static final String BLUETOOTH_NAME = "HC-05";
    private CompoundButton activeBluetooth;
    private CompoundButton activeBracelet;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice braceletDevice;
    private BluetoothConnector bluetoothConnector;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        this.context = this;

        activeBluetooth = (CompoundButton) findViewById(R.id.switch_activate_bluetooth);
        activeBracelet = (CompoundButton) findViewById(R.id.switch_activate_bracelet);

        activeBluetooth.setOnClickListener(onClickActiveBluetooth);
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

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            Log.e("ERROR", "Unable to unregister mReceiver");
        }

        bluetoothAdapter.cancelDiscovery();
        bluetoothConnector.cancel();
        super.onDestroy();
    }

    View.OnClickListener onClickActiveBluetooth = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (initBluetoothAdapter()) {
                activeBluetooth();
            }
        }
    };

    View.OnClickListener onClickActiveBracelet = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (activeBracelet.isChecked()) {

                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    getBraceletDevice();

                    if (braceletDevice != null) {
                        Log.i("BLUETOOTH_CONNECTOR", "Calling thread to connect bluetooth");
                        bluetoothConnector = new BluetoothConnector(context, braceletDevice, bluetoothAdapter);
                        bluetoothConnector.start();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Pulseira não encontrada nos dispositivos pareados", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    activeBracelet.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Conecte-se ao bluetooth", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private boolean initBluetoothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Dispositivo não possui bluetooth!", Toast.LENGTH_LONG).show();
            Log.i("INIT_BLUETOOTH", "Device without bluetooth");
            finish();
            return false;
        }

        return true;
    }

    private void activeBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Log.i("ACTIVE_BLUETOOTH", "Starting bluetooth");
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
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
}
