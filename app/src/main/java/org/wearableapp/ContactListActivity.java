package org.wearableapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
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

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(ContactListActivity.this, RegisterUserActivity.class);

            intent.putExtra("contact_email", adapter.getItemEmail(i));
            startActivity(intent);
        }
    };
}
