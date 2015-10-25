package org.wearableapp.users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.wearableapp.MenuActivity;
import org.wearableapp.R;

import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends Activity {

    private ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Log.i("USER_CONNECTED", "Email is: " + email);

        List<HashMap<String, String>> contacts = Contact.list(email);
        adapter = new ContactListAdapter(this, contacts);

        if (contacts.isEmpty()) {
            findViewById(R.id.no_contacts).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.no_contacts).setVisibility(View.GONE);
        }

        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClick);
        listView.setOnItemLongClickListener(onItemLongClick);

        ImageButton floatButton = (ImageButton) findViewById(R.id.add_contact);
        floatButton.setOnClickListener(onClick);

        getActionBar().setHomeButtonEnabled(true);
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

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(ContactListActivity.this, ContactLevelActivity.class);
            intent.putExtra("type", "update");
            intent.putExtra("contact_name", adapter.getItemName(i));
            intent.putExtra("contact_email", adapter.getItemEmail(i));
            intent.putExtra("contact_level", adapter.getItemLevel(i));

            startActivity(intent);
        }
    };

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ContactListActivity.this, ContactLevelActivity.class);
            intent.putExtra("type", "new");
            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener onItemLongClick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            HashMap hm = (HashMap) adapterView.getItemAtPosition(i);
            String email_contact = hm.get("email").toString();

            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
            String email_user = sharedPreferences.getString("email", "");

            if (Contact.delete(email_user, email_contact)) {
                Log.i("LONG_CLICK", "Contact removed from list");
            } else {
                Log.e("LONG_CLICK", "Error: contact not removed");
            }

            return true;
        }
    };
}
