package org.wearableapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotificationActivity extends Activity {

    private TextView notification;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notification = (TextView) findViewById(R.id.notification_textView);

        if (getIntent().getExtras() != null) {
            notification.setText(getIntent().getExtras().getString("notification"));
        }

        back = (Button) findViewById(R.id.notification_menu_button);
        back.setOnClickListener(onClickBack);
    }

    View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            goToMenu();
        }
    };

    private void goToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToMenu();
        super.onBackPressed();
    }
}
