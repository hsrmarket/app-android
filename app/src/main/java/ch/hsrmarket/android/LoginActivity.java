package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Account;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ApiClient.OnResponseListener, ApiClient.OnFailureListener {

    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                ApiClient apiClient = new ApiClient(getApplicationContext(),0,this,this);
                String hash = getHash(etPassword.getText().toString());
                apiClient.checkCredentials(etEmail.getText().toString(), hash);

                break;

            case R.id.login_register:
                finish();
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));

                break;
        }
    }

    protected static String getHash(String plaintext){
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
            Account account = (Account) data;
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getString(R.string.secret_account), account.getJsonObject());
            editor.commit();

            finish();
    }

    @Override
    public void onFailure(String msg, int requestCode) {
        String finalMsg;
        if(msg.contains("Response{")){
            finalMsg = getString(R.string.msg_fail_login);
        }else{
            finalMsg = msg;
        }
        Toast.makeText(getApplicationContext(),finalMsg,Toast.LENGTH_LONG).show();
    }
}
