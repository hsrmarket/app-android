package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
        int requestId = bundle.getInt(getString(R.string.article_pass_id),-1);
        type = (Article.Type) bundle.getSerializable(getString(R.string.article_pass_type));

        ApiClient apiClient = new ApiClient();
        apiClient.setOnResponseListener(this);
        apiClient.requestSingleArticle(requestId);
    }

    @Override
    public void onDataLoaded(Object data) {
        loadExistingArticle(data);

    }

    private void loadExistingArticle(Object data){
        Article article = (Article) data;
        setTitle(article.getName());

        TextInputEditText etName, etDescription, etPrice, etCondition, etCreatedAt, etId,
                etExtra1, etExtra2, etExtra3 ;

        TextInputLayout ilExtra1, ilExtra2, ilExtra3;

        etName = (TextInputEditText) findViewById(R.id.article_name);
        etDescription = (TextInputEditText) findViewById(R.id.article_description);
        etPrice = (TextInputEditText) findViewById(R.id.article_price);
        etCondition = (TextInputEditText) findViewById(R.id.article_condition);
        etCreatedAt = (TextInputEditText) findViewById(R.id.article_created_at);
        etId = (TextInputEditText) findViewById(R.id.article_id);

        TextInputEditText[] basicViews = new TextInputEditText[]{etDescription, etPrice, etCondition, etCreatedAt, etId};

        etName.setVisibility(View.GONE);
        setDisabled(basicViews);

        String[] basicTexts = new String[]{article.getDescription(), article.getPrice(), ""+article.getCondition(), article.getCreatedAt(), ""+article.getId() };
        setText(basicViews, basicTexts);

        TextInputEditText[] extraViews;
        TextInputLayout[] extraHintsLayouts;
        String[] hints, extraTexts;

        TextInputLayout t = (TextInputLayout) findViewById(R.id.article_hint_id);
        t.setHintAnimationEnabled(false);

        switch (type){
            case BOOK:
                Book book = (Book) data;

                etExtra1 = (TextInputEditText) findViewById(R.id.article_extra1);
                etExtra2 = (TextInputEditText) findViewById(R.id.article_extra2);
                etExtra3 = (TextInputEditText) findViewById(R.id.article_extra3);

                ilExtra1 = (TextInputLayout) findViewById(R.id.article_hint_extra1);
                ilExtra2 = (TextInputLayout) findViewById(R.id.article_hint_extra2);
                ilExtra3 = (TextInputLayout) findViewById(R.id.article_hint_extra3);

                extraViews = new TextInputEditText[]{etExtra1, etExtra2, etExtra3};
                extraHintsLayouts = new TextInputLayout[]{ilExtra1, ilExtra2, ilExtra3};

                setVisible(extraViews);
                setDisabled(extraViews);

                hints = new String[]{getString(R.string.article_author), getString(R.string.article_publisher), getString(R.string.article_isbn)};
                setHint(extraHintsLayouts, hints);

                extraTexts = new String[]{book.getAuthor(),book.getPublisher(),book.getISBN()};
                setText(extraViews,extraTexts);

                break;

            case ELECTRONIC_DEVICE:
                ElectronicDevice electronicDevice = (ElectronicDevice) data;

                etExtra1 = (TextInputEditText) findViewById(R.id.article_extra1);
                etExtra2 = (TextInputEditText) findViewById(R.id.article_extra2);

                ilExtra1 = (TextInputLayout) findViewById(R.id.article_hint_extra1);
                ilExtra2 = (TextInputLayout) findViewById(R.id.article_hint_extra2);

                extraViews = new TextInputEditText[]{etExtra1, etExtra2};
                extraHintsLayouts = new TextInputLayout[]{ilExtra1, ilExtra2};

                setVisible(extraViews);
                setDisabled(extraViews);

                hints = new String[]{getString(R.string.article_producer), getString(R.string.article_model)};
                setHint(extraHintsLayouts, hints);

                extraTexts = new String[]{electronicDevice.getProducer(),electronicDevice.getModel()};
                setText(extraViews,extraTexts);

                break;
        }
    }

    private void setDisabled(View[] views){
        for(View v: views){
            v.setEnabled(false);
        }
    }

    private void setVisible(View[] views){
       for(View v: views){
           v.setVisibility(View.VISIBLE);
       }
    }

    private void setHint(TextInputLayout[] views, String[] texts){
        for(int i = 0; i < views.length; i++){
            views[i].setHint(texts[i]);
        }
    }

    private void setText(TextInputEditText[] views, String[] texts){
        for(int i = 0; i < views.length; i++){
            views[i].setText(texts[i]);
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
