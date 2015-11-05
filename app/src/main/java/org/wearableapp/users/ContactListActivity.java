package org.wearableapp.users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.wearableapp.App;
import org.wearableapp.MenuActivity;
import org.wearableapp.R;

import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends Activity {

    private ContactListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        String email = App.getPreferences().getString("email", "");
        Log.i("USER_CONNECTED", "Email: " + email);

        List<HashMap<String, String>> contacts = Contact.list(email);
        mAdapter = new ContactListAdapter(contacts);

        if (contacts.isEmpty()) {
            findViewById(R.id.no_contacts).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.no_contacts).setVisibility(View.GONE);
        }

        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setAdapter(mAdapter);
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
            intent.putExtra("contact_name", mAdapter.getItemName(i));
            intent.putExtra("contact_email", mAdapter.getItemEmail(i));
            intent.putExtra("contact_level", mAdapter.getItemLevel(i));

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
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int id, long l) {
            HashMap hm = (HashMap) adapterView.getItemAtPosition(id);
            final String contact_email = hm.get("email").toString();
            final String user_email = App.getPreferences().getString("email", "");

            AlertDialog.Builder alert = new AlertDialog.Builder(ContactListActivity.this);
            alert.setTitle("Remover contato");
            alert.setMessage("Tem certeza que deseja remover este contato da lista?");
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (Contact.delete(user_email, contact_email)) {
                        mAdapter.removeItem(id);
                        Toast.makeText(getApplicationContext(), "Contato removido",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alert.show();
            return true;
        }
    };
}
