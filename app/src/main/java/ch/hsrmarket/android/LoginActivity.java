package ch.hsrmarket.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.security.MessageDigest;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Person;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ApiClient.OnResponseListener, ApiClient.OnFailureListener {

    private TextInputEditText etEmail, etPassword;

    public static final int LOGIN_REQUEST = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etEmail = (TextInputEditText) findViewById(R.id.login_email);
        etPassword = (TextInputEditText) findViewById(R.id.login_password);

        Button btnLogin = (Button) findViewById(R.id.login_send);
        btnLogin.setOnClickListener(this);
        Button btnRegister = (Button) findViewById(R.id.login_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_send:
                ApiClient apiClient = new ApiClient(getApplicationContext(),LOGIN_REQUEST,this,this);
                String hash = getHash(etPassword.getText().toString());
                apiClient.checkCredentials(etEmail.getText().toString(), hash);

                break;

            case R.id.login_register:
                finish();
                //TODO start RegisterActivity

                break;
        }
    }

    private String getHash(String plaintext){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plaintext.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        if(requestCode == LOGIN_REQUEST){

            Person person = (Person) data;
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt(getString(R.string.login_person_id), person.getId());
            editor.putString(getString(R.string.login_person_email), person.getEmail());
            editor.putString(getString(R.string.login_person_password),getHash(etPassword.getText().toString()));
            editor.commit();

            finish();

        }
    }

    @Override
    public void onFailure(String msg, int requestCode) {
        if(requestCode == LOGIN_REQUEST){
            etPassword.setError(getString(R.string.msg_fail_login));
        }
    }
}
