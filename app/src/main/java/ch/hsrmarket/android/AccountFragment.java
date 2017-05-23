package ch.hsrmarket.android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Address;


public class AccountFragment extends Fragment implements View.OnClickListener, ApiClient.OnResponseListener, ApiClient.OnFailureListener{

    private TextInputEditText etStudentId, etFirstName, etLastName, etStreet, etStreetNo,
            etZip, etCity, etPhone, etEmail, etPassword, etPasswordConfirm;

    public static final int DISPLAY_EMPTY = 3;
    public static final int DISPLAY_ONLY = 5;
    public static final int DISPLAY_EDITABLE = 8;

    private int displayMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        Button btnSend = (Button) rootView.findViewById(R.id.register_send);
        btnSend.setOnClickListener(this);

        etStudentId = (TextInputEditText) rootView.findViewById(R.id.register_student_id);
        etFirstName = (TextInputEditText) rootView.findViewById(R.id.register_first_name);
        etLastName = (TextInputEditText) rootView.findViewById(R.id.register_last_name);
        etStreet = (TextInputEditText) rootView.findViewById(R.id.register_street);
        etStreetNo = (TextInputEditText) rootView.findViewById(R.id.register_street_no);

        etZip = (TextInputEditText) rootView.findViewById(R.id.register_zip);
        etCity = (TextInputEditText) rootView.findViewById(R.id.register_city);
        etPhone = (TextInputEditText) rootView.findViewById(R.id.register_phone);
        etEmail = (TextInputEditText) rootView.findViewById(R.id.register_email);
        etPassword = (TextInputEditText) rootView.findViewById(R.id.register_password);
        etPasswordConfirm = (TextInputEditText) rootView.findViewById(R.id.register_password_confirm);

        Bundle bundle = getArguments();
        displayMode = bundle.getInt(getString(R.string.account_display_mode),-1);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_send:

                EditText[] others = new EditText[]{etStudentId,etFirstName,etLastName,etStreet,etStreetNo,etZip,etCity,etPhone,etEmail,etPassword};

                if(!isEmpty(others) && isValidEmail(etEmail) && isPasswordOK(etPassword,etPasswordConfirm)){
                    ApiClient apiClient = new ApiClient(getContext(),0,this,this);

                    String hash = LoginActivity.getHash(getString(etPassword));
                    Account account = new Account(getInt(etStudentId),getString(etFirstName),getString(etLastName),getAddress(etStreet,etStreetNo,etZip,etCity), getString(etPhone), getString(etEmail), hash);

                    apiClient.postAccount(account);
                }

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

    private Address getAddress(EditText street, EditText streetNo, EditText zip, EditText city){
        return new Address(getString(street),getString(streetNo),getInt(zip),getString(city));
    }

    private String getString(EditText editText){
        return editText.getText().toString();
    }

    private int getInt(EditText editText){
        return Integer.parseInt(editText.getText().toString());
    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        Account account = (Account) data;
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_credentials), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.secret_account), account.getJsonObject());
        editor.commit();

        getActivity().finish();
    }

    @Override
    public void onFailure(String msg, int requestCode) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }

}
