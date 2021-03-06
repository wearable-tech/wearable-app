package org.wearableapp.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.wearableapp.App;
import org.wearableapp.R;

import java.util.HashMap;
import java.util.List;

public class ContactListAdapter extends BaseAdapter {

    private List<HashMap<String, String>> items;

    public ContactListAdapter(List<HashMap<String, String>> contacts) {
        this.items = contacts;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public String getItemName(int i) {
        return items.get(i).get("name");
    }

    public String getItemEmail(int i) {
        return items.get(i).get("email");
    }

    public String getItemLevel(int i) {
        return items.get(i).get("level");
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(App.getContext()).inflate(R.layout.listitem_contact, null);
        }

        TextView emailTextView = (TextView) view.findViewById(R.id.textview_email_contact);
        TextView levelTextView = (TextView) view.findViewById(R.id.textview_level_contact);

        emailTextView.setText(getItemEmail(i));
        levelTextView.setText("Nível: " + getItemLevel(i));

        return view;
    }

    public void removeItem(int i) {
        items.remove(i);
        this.notifyDataSetChanged();
    }
}
