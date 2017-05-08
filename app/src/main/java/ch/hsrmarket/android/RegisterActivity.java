package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etStudentId, etFirstName, etLastName, etStreet, etStreetNo,
            etZip, etCity, etPhone, etEmail, etPassword, etPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnSend = (Button) findViewById(R.id.register_send);
        btnSend.setOnClickListener(this);

        etStudentId = (TextInputEditText) findViewById(R.id.register_studentId);
        etFirstName = (TextInputEditText) findViewById(R.id.register_firstName);
        etLastName = (TextInputEditText) findViewById(R.id.register_lastName);
        etStreet = (TextInputEditText) findViewById(R.id.register_street);
        etStreetNo = (TextInputEditText) findViewById(R.id.register_streetNo);

        etZip = (TextInputEditText) findViewById(R.id.register_zip);
        etCity = (TextInputEditText) findViewById(R.id.register_city);
        etPhone = (TextInputEditText) findViewById(R.id.register_phone);
        etEmail = (TextInputEditText) findViewById(R.id.register_email);
        etPassword = (TextInputEditText) findViewById(R.id.register_password);
        etPasswordConfirm = (TextInputEditText) findViewById(R.id.register_passwordConfirm);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_send:

                EditText[] others = new EditText[]{etStudentId,etFirstName,etLastName,etStreet,etStreetNo,etZip,etCity,etPhone};
                isEmpty(others);
                isValidEmail(etEmail);
                isPasswordOK(etPassword,etPasswordConfirm);

                break;
        }
    }

    private boolean isEmpty(EditText[] editTexts){
        boolean retVal = false;

        for(EditText et : editTexts){
            if(et.getText().toString().isEmpty()){
                et.setError(getString(R.string.msg_form_empty));
                retVal = true;
            }
        }

        return retVal;
    }

    private boolean isValidEmail(EditText editText){
        CharSequence target = editText.getText().toString();
        boolean retVal = Patterns.EMAIL_ADDRESS.matcher(target).matches();

        if(!retVal){
            editText.setError(getString(R.string.msg_form_invalid_email));
        }

        return retVal;
    }

    private boolean isPasswordOK(EditText pass1, EditText pass2){
        String txtPass1 = pass1.getText().toString();
        String txtPass2 = pass2.getText().toString();

        if(txtPass1.length() < 8){
            pass1.setError(getString(R.string.msg_form_password_length));
            return false;
        }

        if(!txtPass1.equals(txtPass2)){
            pass2.setError(getString(R.string.msg_form_password_match));
            return false;
        }

        return  true;
    }
}
