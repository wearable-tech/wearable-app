package org.wearableapp.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.wearableapp.App;
import org.wearableapp.MenuActivity;
import org.wearableapp.R;
import org.wearableapp.services.BluetoothService;

import java.util.Set;

public class BluetoothActivity extends Activity {

    private static final String BLUETOOTH_NAME = "HC-05";
    private static final int REQUEST_ENABLE_BT = 2;
    private CompoundButton activeBluetooth;
    private CompoundButton activeWerable;
    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        isBluetoothSupported();

        activeBluetooth = (CompoundButton) findViewById(R.id.switch_activate_bluetooth);
        activeBluetooth.setOnClickListener(onClickActiveBluetooth);
        activeBluetooth.setChecked(bluetoothAdapter.isEnabled());

        activeWerable = (CompoundButton) findViewById(R.id.switch_activate_werable);
        activeWerable.setOnClickListener(onClickActiveWearable);

        getActionBar().setHomeButtonEnabled(true);

        activeWerable.setChecked(App.getPreferences().getBoolean("werableStatus", false));
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
                Intent intent = new Intent(App.getContext(), BluetoothService.class);
                stopService(intent);
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
            activeWerable.setChecked(false);
            setWearableStatus(false);
        }
    }

    View.OnClickListener onClickActiveWearable = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(App.getContext(), BluetoothService.class);

            if (activeWerable.isChecked() && activeBluetooth.isChecked()) {

                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    setBluetoothDevice();

                    if (bluetoothDevice != null) {
                        startService(intent);
                        setWearableStatus(true);
                    }
                    else {
                        stopService(intent);
                        setWearableStatus(false);
                        Toast.makeText(getApplicationContext(), "Pulseira não encontrada nos dispositivos pareados", Toast.LENGTH_LONG).show();
                        activeWerable.setChecked(false);
                    }
                }
                else {
                    stopService(intent);
                    setWearableStatus(false);
                    activeWerable.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Bluetooth desligado", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private void setWearableStatus(boolean status) {
        SharedPreferences.Editor editor = App.getPreferences().edit();
        if (status)
            editor.putBoolean("werableStatus", status);
        else
            editor.remove("werableStatus");
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            activeBluetooth.setChecked(bluetoothAdapter.isEnabled());
        }
    }

    public static BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public static BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    private void setBluetoothDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        Log.i("PAIRED_DEVICES", "Paired devices found " + pairedDevices.size());
        for (BluetoothDevice device : pairedDevices) {
            Log.i("DEVICE_PAIRED", "Name: " + device.getName() + " Address: " + device.getAddress());

            if (device.getName().contains(BLUETOOTH_NAME)) {
                bluetoothDevice = device;
                break;
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
