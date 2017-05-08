package ch.hsrmarket.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Article;
import ch.hsrmarket.android.model.Book;
import ch.hsrmarket.android.model.ElectronicDevice;
import ch.hsrmarket.android.model.Person;

public class ArticleActivity extends AppCompatActivity implements ApiClient.OnResponseListener, View.OnClickListener, ApiClient.OnFailureListener {

    private Article.Type type;
    private Article currentArticle;

    public static final int ARTICLE_REQUEST = 5;
    public static final int PURCHASE_POST = 8;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_buy);
        fab.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        int requestId = bundle.getInt(getString(R.string.article_pass_id),-1);
        type = (Article.Type) bundle.getSerializable(getString(R.string.article_pass_type));

        ApiClient apiClient = new ApiClient(getApplicationContext(),ARTICLE_REQUEST,this,this);
        apiClient.requestSingleArticle(requestId);
    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        switch (requestCode){
            case ARTICLE_REQUEST:
                loadExistingArticle(data);
                break;
            case PURCHASE_POST:
                Toast.makeText(getApplicationContext(),getString(R.string.msg_successful_purchased),Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onFailure(String msg, int requestCode) {
        switch (requestCode){
            case ARTICLE_REQUEST:
            case PURCHASE_POST:
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void loadExistingArticle(Object data){
        currentArticle = (Article) data;
        setTitle(currentArticle.getName());

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

        String[] basicTexts = new String[]{currentArticle.getDescription(), currentArticle.getPrice(), ""+currentArticle.getCondition(), currentArticle.getCreatedAt(), ""+currentArticle.getId() };
        setText(basicViews, basicTexts);

        TextInputEditText[] extraViews;
        TextInputLayout[] extraHintsLayouts;
        String[] hints, extraTexts;

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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fab_buy:

                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_credentials), Context.MODE_PRIVATE);
                int personId = sharedPref.getInt(getString(R.string.login_person_id),-1);
                Person person = new Person(personId);

                ApiClient apiClient = new ApiClient(getApplicationContext(),PURCHASE_POST,this,this);
                apiClient.createPurchase(currentArticle,person);

                break;
        }

    }

}
