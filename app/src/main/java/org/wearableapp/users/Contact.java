package org.wearableapp.users;

import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wearableapp.communications.HttpRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Contact {

    public static ArrayList<HashMap<String, String>> list(String email) {
        ArrayList<HashMap<String, String>> contacts = new ArrayList<>();
        List params = new ArrayList();
        params.add(new BasicNameValuePair("email", email));

        if (HttpRequests.doPost(params, "/user/contacts.json") == 0) {
            Log.i("JSON LIST", HttpRequests.getResponse());
            try {
                JSONArray jsonArray = new JSONArray(HttpRequests.getResponse());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("name", object.getString("name"));
                    contact.put("email", object.getString("email"));
                    contact.put("level", object.getString("level"));

                    contacts.add(contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("CONTACTS_LIST", "Contacts found: " + contacts.size());
        return contacts;
    }
}
