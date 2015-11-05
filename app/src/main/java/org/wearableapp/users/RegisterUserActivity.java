package org.wearableapp.users;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.wearableapp.R;
import org.wearableapp.communications.HttpRequests;

import java.util.ArrayList;
import java.util.List;

public class RegisterUserActivity extends Activity {

    private Button saveNewUser;
    private Button cancelNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        cancelNewUser = (Button) findViewById(R.id.button_cancel_new_user);
        saveNewUser = (Button) findViewById(R.id.button_new_user);

        cancelNewUser.setOnClickListener(onClickCancel);
        saveNewUser.setOnClickListener(onClickSave);
    }

    View.OnClickListener onClickSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText name = (EditText) findViewById(R.id.edittext_name_new_user);
            EditText email = (EditText) findViewById(R.id.edittext_email_new_user);
            EditText password = (EditText) findViewById(R.id.edittext_password_new_user);

            List params = new ArrayList();
            params.add(name);
            params.add(email);
            params.add(password);

            if (!validateUser((ArrayList<EditText>) params)) {
                Toast.makeText(getApplicationContext(), "Cheque os campos", Toast.LENGTH_LONG).show();
                return;
            }

            params.clear();
            params.add(new BasicNameValuePair("name", name.getText().toString()));
            params.add(new BasicNameValuePair("email", email.getText().toString()));
            params.add(new BasicNameValuePair("password", password.getText().toString()));

            if (HttpRequests.doPost(params, "/user/save") == 0) {
                Log.i("CREATE_USER", "Create user success");
                Toast.makeText(getApplicationContext(), "Usuário cadastrado!", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Log.e("CREATE_USER", "Create user error");
                Toast.makeText(getApplicationContext(), "Erro, tente novamente!", Toast.LENGTH_LONG).show();
            }
        }
    };

    private boolean validateUser(ArrayList<EditText> user) {
        if (!UserValidation.name(user.get(0))) return false;
        if (!UserValidation.email(user.get(1))) return false;
        if (!UserValidation.password(user.get(2))) return false;

        return true;
    }

    View.OnClickListener onClickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
