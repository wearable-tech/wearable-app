package org.wearableapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends Activity {

    private List<HashMap<String, String>> contacts;
    private ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Log.i("USER_CONNECTED", "Email is: " + email);

        contacts = Contact.list(email);
        adapter = new ContactListAdapter(this, contacts);

        ListView userListView = (ListView) findViewById(R.id.contact_list);
        userListView.setAdapter(adapter);
        userListView.setOnItemClickListener(onItemClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_new_contact) {
            Intent intent = new Intent(ContactListActivity.this, ContactLevelActivity.class);
            intent.putExtra("type", "new");
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(ContactListActivity.this, ContactLevelActivity.class);
            intent.putExtra("type", "update");
            intent.putExtra("contact_email", adapter.getItemEmail(i));
            intent.putExtra("contact_level", adapter.getItemLevel(i));

            startActivity(intent);
        }
    };
}
