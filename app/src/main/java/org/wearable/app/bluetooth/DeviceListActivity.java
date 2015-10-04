package org.wearable.app.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.wearable.app.R;

import java.util.HashSet;
import java.util.Set;

public class DeviceListActivity extends Activity {
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private DeviceConnector mDeviceConnector = new NullDeviceConnector();
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private Button scanBtn;
    private final Set<String> mNewDevicesSet = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

        // Set default result to CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        boolean noAvailableDevices = true;

        //get paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // put it's one to the adapter
        if (pairedDevices != null && !pairedDevices.isEmpty()) {
            // create the arrayAdapter that contains the BTDevices, and set it to the ListView
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
            ListView pairedListView = (ListView) findViewById(R.id.paired_devices);

            pairedListView.setAdapter(arrayAdapter);
            pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    final String info = ((TextView) view).getText().toString();
                    String name = info.substring(0, info.length() - 17);
                    String address = info.substring(info.length() - 17);

                    Log.i("BT", "name: " + name);
                    Log.i("BT", "address: " + address);

                    mDeviceConnector = new BluetoothDeviceConnector(address);
                    mDeviceConnector.connect();
                }
            });

            registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

            for (BluetoothDevice device : pairedDevices) {
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }

            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            noAvailableDevices = false;
        }

        if (noAvailableDevices) {
            findViewById(R.id.label_none_found).setVisibility(View.VISIBLE);
        }

        // Creates a new list with new devices found
        mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                final String info = ((TextView) view).getText().toString();
                String name = info.substring(0, info.length() - 17);
                String address = info.substring(info.length() - 17);

                Log.i("BT", "name: " + name);
                Log.i("BT", "address: " + address);

                mDeviceConnector = new BluetoothDeviceConnector(address);
                mDeviceConnector.connect();
            }
        });

        IntentFilter btDeviceFoundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, btDeviceFoundFilter);

        IntentFilter discoveryFinishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, discoveryFinishedFilter);

        scanBtn = (Button) findViewById(R.id.button_scan);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();
                view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Start device discovery with the BluetoothAdapter
     */
    private void doDiscovery() {
        Log.i("BT", "Discovering...");

        mNewDevicesArrayAdapter.clear();
        mNewDevicesSet.clear();

        setProgressBarIndeterminateVisibility(true);

        findViewById(R.id.label_none_found).setVisibility(View.GONE);
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        findViewById(R.id.label_scanning).setVisibility(View.VISIBLE);

        if (mBluetoothAdapter.isDiscovering()) {
            // the button is pressed when it discovers, so cancel the discovery
            mBluetoothAdapter.cancelDiscovery();
        }

        mBluetoothAdapter.startDiscovery();
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    String name = device.getName();
                    String address = device.getAddress();
                    if (!mNewDevicesSet.contains(address)) {
                        // add the name and the MAC address of the object to the arrayAdapter
                        mNewDevicesArrayAdapter.add(name + "\n" + address);
                        mNewDevicesArrayAdapter.notifyDataSetChanged();
                        mNewDevicesSet.add(address);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i("BT", "Discovery finished");
                setProgressBarIndeterminateVisibility(false);
                findViewById(R.id.label_scanning).setVisibility(View.GONE);
                scanBtn.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
    }
}
