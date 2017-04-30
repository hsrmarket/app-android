package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Article;
import ch.hsrmarket.android.model.Book;

public class ArticleActivity extends AppCompatActivity implements ApiClient.OnResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_buy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        int requestId = bundle.getInt("id",-1);

        ApiClient apiClient = new ApiClient();
        apiClient.setOnResponseListener(this);
        apiClient.requestSingleArticle(requestId);
    }

    @Override
    public void onDataLoaded(Object data) {
        Book m = (Book) data;
        System.out.println(""+m.toString());
    }
}
