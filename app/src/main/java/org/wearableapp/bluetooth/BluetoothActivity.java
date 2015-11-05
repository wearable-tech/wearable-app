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

import org.wearableapp.MenuActivity;
import org.wearableapp.R;

public class BluetoothActivity extends Activity {
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private TextView mStatus;
    private BluetoothAdapter mBluetoothAdapter;
    private Button mlistBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mStatus = (TextView) findViewById(R.id.btstatus);

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
                    Toast.makeText(getApplicationContext(), "Bluetooth foi desligado",
                            Toast.LENGTH_LONG).show();
                    mlistBtn.setEnabled(false);
                    mStatus.setText("Status: desligado");
                }
            }
        });

        // take an instance of BluetoothAdapter - Bluetooth radio
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            mlistBtn.setEnabled(false);
            mStatus.setText("Status: BT não suportado");

            Toast.makeText(getApplicationContext(), "Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
        } else if (mBluetoothAdapter.isEnabled()) {
            mStatus.setText("Status: ligado");
        } else {
            mlistBtn.setEnabled(false);
            mStatus.setText("Status: desligado");
        }

        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Bluetooth foi ligado" ,
                        Toast.LENGTH_LONG).show();
                mlistBtn.setEnabled(true);
                mStatus.setText("Status: ligado");
            } else {
                mStatus.setText("Status: desligado");
            }
        }
    }

    public void on(View view) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        } else {
            mlistBtn.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Bluetooth já está ligado",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void off(View view) {
        mBluetoothAdapter.disable();
        mStatus.setText("Status: desligado");
        mlistBtn.setEnabled(false);

        Toast.makeText(getApplicationContext(), "Bluetooth foi desligado",
                Toast.LENGTH_LONG).show();
    }

    public void list(View view) {
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
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
}