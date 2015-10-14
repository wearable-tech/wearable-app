package org.wearableapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactLevelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_level);

        saveContactLevel(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_level, menu);
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

    private void saveContactLevel(final Context context) {
        Button saveContactLevel = (Button) findViewById(R.id.saveContactLevel);
        saveContactLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("SAVE_CONTACT_LEVEL", "Saving level to contact");

                EditText level = (EditText) findViewById(R.id.levelContact);
                EditText contactEmail = (EditText) findViewById(R.id.contactEmail);

                Log.i("CONTACT_LEVEL", level.getText().toString());
                Log.i("CONTACT_EMAIL", contactEmail.getText().toString());
            }
        });
    }
}
