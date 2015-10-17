package org.wearableapp;

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

    private static ArrayList<HashMap<String, String>> contacts;

    public static ArrayList<HashMap<String, String>> list(String email) {
        contacts = new ArrayList<HashMap<String, String>>();

        List params = new ArrayList();
        params.add(new BasicNameValuePair("email", email));

        if (HttpRequests.doPost(params, "/user/contacts.json")) {
            Log.i("JSON LIST", HttpRequests.getResponse());
            try {
                JSONArray jsonArray = new JSONArray(HttpRequests.getResponse());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    String emailContact = object.getString("email");
                    String levelContact = object.getString("level");

                    HashMap<String, String> contact = new HashMap<String, String>();
                    contact.put("email", emailContact);
                    contact.put("level", levelContact);

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
