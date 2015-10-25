package org.wearableapp.users;

import android.widget.EditText;

public class UserValidation {
    private static final String REQUIRED_MSG = "Campo obrigatório";
    private static final String INVALID_EMAIL = "E-mail inválido";
    private static final String SHORT_PASSWD = "Pelo menos 4 dígitos";

    public static boolean name(EditText name) {
        return hasText(name);
    }

    public static boolean email(EditText email) {
        String text = email.getText().toString().trim();
        email.setError(null);

        if (!hasText(email)) return false;

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            email.setError(INVALID_EMAIL);
            return false;
        }

        return true;
    }

    public static boolean password(EditText password) {
        String text = password.getText().toString();
        password.setError(null);

        if (text.length() < 4) {
            password.setError(SHORT_PASSWD);
            return false;
        }

        return true;
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
