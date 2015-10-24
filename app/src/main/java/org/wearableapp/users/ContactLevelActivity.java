package org.wearableapp.users;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.wearableapp.R;
import org.wearableapp.communications.HttpRequests;
import org.wearableapp.users.ContactListActivity;
import org.wearableapp.users.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactLevelActivity extends Activity {

    private EditText contactEmail;
    private EditText contactName;
    private EditText contactLevel;
    private Button saveContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_level);

        contactName = (EditText) findViewById(R.id.edittext_contact_name);
        contactEmail = (EditText) findViewById(R.id.edittext_contact_email);
        contactLevel = (EditText) findViewById(R.id.edittext_contact_level);
        saveContact = (Button) findViewById(R.id.button_save_contact);

        String type = (String) getIntent().getExtras().getSerializable("type");
        Log.i("REGISTER", "Type register is: " + type);

        if (type.contains("update")) {
            String contactNameSaved = (String) getIntent().getExtras().getSerializable("contact_name");
            String contactEmailSaved = (String) getIntent().getExtras().getSerializable("contact_email");
            String contacLevelSaved = (String) getIntent().getExtras().getSerializable("contact_level");
            Log.i("REGISTER", "Update contact " + contactEmailSaved);

            contactName.setText(contactNameSaved);
            contactName.setFocusable(false);
            contactEmail.setText(contactEmailSaved);
            contactEmail.setFocusable(false);
            contactLevel.setText(contacLevelSaved);
        }
        else {
            TextView contactNameTextView = (TextView) findViewById(R.id.textview_contact_name);
            contactNameTextView.setVisibility(View.INVISIBLE);
            contactName.setVisibility(View.INVISIBLE);
        }

        saveContact.setOnClickListener(onClickSaveContact);
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

    View.OnClickListener onClickSaveContact = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("CONTACT_LEVEL", "Saving level to contact");
            addContact(contactEmail.getText().toString(), contactLevel.getText().toString());
        }
    };

    private void goToContacsList() {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
        finish();
    }

    private void addContact(String contact_email, String contact_level) {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        String user_email = sharedPreferences.getString("email", "");

        List params = new ArrayList();
        params.add(new BasicNameValuePair("user_email", user_email));
        params.add(new BasicNameValuePair("contact_email", contact_email));
        params.add(new BasicNameValuePair("contact_level", contact_level));

        if (HttpRequests.doPost(params, "/user/add_contact") == 0) {
            Log.i("ADD_CONTACT", "Success in add contact " + contact_email);
            goToContacsList();
        }
        else {
            Log.i("ADD_CONTACT", "Fail in add contact " + contact_email);
            Toast.makeText(getApplicationContext(), "Não foi possível adicionar este contato", Toast.LENGTH_LONG).show();
        }
    }

}
