package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Article;
import ch.hsrmarket.android.model.Book;
import ch.hsrmarket.android.model.ElectronicDevice;

public class ArticleActivity extends AppCompatActivity implements ApiClient.OnResponseListener {

    private Article.Type type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_buy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        int requestId = bundle.getInt(getString(R.string.article_id),-1);
        type = (Article.Type) bundle.getSerializable(getString(R.string.article_type));

        ApiClient apiClient = new ApiClient();
        apiClient.setOnResponseListener(this);
        apiClient.requestSingleArticle(requestId);
    }

    @Override
    public void onDataLoaded(Object data) {
        Article article = (Article) data;

        setTitle(article.getName());

        TextView tvDescription, tvPrice, tvCondition, tvCreatedAt, tvId,
                 tvExtra1, tvExtra2, tvExtra3 ;

        tvDescription = (TextView) findViewById(R.id.article_description);
        tvPrice = (TextView) findViewById(R.id.article_price);
        tvCondition = (TextView) findViewById(R.id.article_condition);
        tvCreatedAt = (TextView) findViewById(R.id.article_createdAt);
        tvId = (TextView) findViewById(R.id.article_id);

        tvDescription.setText(article.getDescription());
        tvPrice.setText(article.getPrice());
        tvCondition.setText(""+article.getCondition());
        tvCreatedAt.setText(article.getCreatedAt());
        tvId.setText(""+article.getId());

        switch (type){
            case BOOK:
                Book book = (Book) data;

                tvExtra1 = (TextView) findViewById(R.id.article_extra1);
                tvExtra2 = (TextView) findViewById(R.id.article_extra2);
                tvExtra3 = (TextView) findViewById(R.id.article_extra3);

                tvExtra1.setVisibility(View.VISIBLE);
                tvExtra2.setVisibility(View.VISIBLE);
                tvExtra3.setVisibility(View.VISIBLE);

                tvExtra1.setText(book.getAuthor());
                tvExtra2.setText(book.getPublisher());
                tvExtra3.setText(book.getISBN());

                break;

            case ELECTRONIC_DEVICE:
                ElectronicDevice electronicDevice = (ElectronicDevice) data;

                tvExtra1 = (TextView) findViewById(R.id.article_extra1);
                tvExtra2 = (TextView) findViewById(R.id.article_extra2);

                tvExtra1.setVisibility(View.VISIBLE);
                tvExtra2.setVisibility(View.VISIBLE);

                tvExtra1.setText(electronicDevice.getProducer());
                tvExtra2.setText(electronicDevice.getModel());

                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
