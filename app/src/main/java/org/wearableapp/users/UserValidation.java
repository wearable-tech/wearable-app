package org.wearableapp.users;

import android.util.Log;
import android.widget.EditText;

import org.wearableapp.App;

public class UserValidation {
    private static final String REQUIRED_MSG = "Campo obrigatório";
    private static final String INVALID_EMAIL = "E-mail inválido";
    private static final String SHORT_PASSWORD = "Pelo menos 4 dígitos";
    private static final String WRONG_PASSWORD = "Senha incorreta";

    public static boolean name(EditText name) {
        return hasText(name);
    }

    public static boolean email(EditText email) {
        String text = email.getText().toString().trim();
        email.setError(null);

        if (!hasText(email)) return false;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            email.setError(INVALID_EMAIL);
            return false;
        }

        return true;
    }

    public static boolean password(EditText password) {
        String text = password.getText().toString();
        password.setError(null);

        if (text.length() < 4) {
            password.setError(SHORT_PASSWORD);
            return false;
        }

        return true;
    }

    public static boolean comparePasswords(EditText etCurrentPassword, EditText newPassword) {
        String currentPassword = etCurrentPassword.getText().toString();
        etCurrentPassword.setError(null);

        if (currentPassword.length() < 0) {
            return true;
        }

        String oldPassword = App.getPreferences().getString("password", "");

        Log.i("VALIDATION", "System password: " + oldPassword);
        Log.i("VALIDATION", "Current password: " + currentPassword);
        Log.i("VALIDATION", "New password: " + newPassword.getText().toString());
        if (!currentPassword.equals(oldPassword)) {
            etCurrentPassword.setError(WRONG_PASSWORD);
            return false;
        }

        return password(newPassword);
    }

    private static boolean hasText(EditText editText) {
        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

}
