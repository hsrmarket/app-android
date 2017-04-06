package ch.hsrmarket.android;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HSRMARKET_API";

    private ApiInterface apiService;
    private RecyclerView recyclerView;
    private EditText etId;
    private EditText etISBN;
    private EditText etAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.hsrmarket.android.R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.listview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getClient().create(ApiInterface.class);

        updateData();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                updateData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        etId = (EditText) findViewById(R.id.et_id);
        etISBN = (EditText) findViewById(R.id.et_isbn);
        etAuthor = (EditText) findViewById(R.id.et_author);

        Button btnPost = (Button) findViewById(R.id.btn_post);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etId.getText().toString().isEmpty() || etISBN.getText().toString().isEmpty() || etAuthor.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fields mustn't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    int id = Integer.valueOf(etId.getText().toString());
                    Book b = new Book(id,etISBN.getText().toString(),etAuthor.getText().toString());

                    postBook(b);
                }

            }
        });

    }

    private void updateData(){
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                if(response.isSuccessful()){
                    List<Book>  books = response.body();
                    Collections.reverse(books);
                    recyclerView.setAdapter(new BookAdapter(books,R.layout.list_item,getApplicationContext()));

                    etId.setText("" + (books.get(0).getId()+1) );

                }else {

                    Log.e(TAG,response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void postBook(Book book){
        Call<Book> call = apiService.createBook(book);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response.isSuccessful()){
                    emptyFields();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void emptyFields(){
        etISBN.setText("");
        etAuthor.setText("");

        updateData();

    }
}
