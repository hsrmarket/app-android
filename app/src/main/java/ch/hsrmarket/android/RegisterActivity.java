package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity  {

    private TextInputEditText etStudentId, etFirstName, etLastName, etStreet, etStreetNo,
            etZip, etCity, etPhone, etEmail, etPassword, etPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountFragment fragment = new AccountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.account_display_mode),AccountFragment.DISPLAY_EMPTY);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.register_base_layout,fragment);
        fragmentTransaction.commit();
    }


}
