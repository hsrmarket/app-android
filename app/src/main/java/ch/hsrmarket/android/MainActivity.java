package ch.hsrmarket.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import ch.hsrmarket.android.api.ApiClient;

public class MainActivity extends AppCompatActivity {
    private EditText etId;
    private EditText etISBN;
    private EditText etAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.hsrmarket.android.R.layout.activity_main);


        etId = (EditText) findViewById(R.id.et_id);
        etISBN = (EditText) findViewById(R.id.et_isbn);
        etAuthor = (EditText) findViewById(R.id.et_author);

        ApiClient apiClient = new ApiClient();
        apiClient.testReq();


    }


}
