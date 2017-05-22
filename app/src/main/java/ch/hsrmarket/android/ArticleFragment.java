package ch.hsrmarket.android;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Article;
import ch.hsrmarket.android.model.Book;
import ch.hsrmarket.android.model.Electronic;
import ch.hsrmarket.android.model.Purchase;

import static ch.hsrmarket.android.ArticleActivity.DISPLAY_ONLY;
import static ch.hsrmarket.android.ArticleActivity.DISPLAY_PURCHASE;
import static ch.hsrmarket.android.ArticleActivity.DISPLAY_WITH_BUY;

public class ArticleFragment extends Fragment implements ApiClient.OnResponseListener, ApiClient.OnFailureListener, View.OnClickListener {

    private FloatingActionButton fab;
    private Article.Type type;
    private Article currentArticle;
    private View rootView;

    public static final int GET_ARTICLE = 1;
    public static final int POST_PURCHASE = 2;
    public static final int GET_PURCHASE = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_article, container, false);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_buy);
        fab.setOnClickListener(this);

        Bundle bundle = getArguments();
        int receivedArticleId = bundle.getInt(getString(R.string.article_pass_id),-1);
        type = (Article.Type) bundle.getSerializable(getString(R.string.article_pass_type));
        int receivedPurchaseId = bundle.getInt(getString(R.string.article_pass_purchase_id),-1);

        int chosenDisplay = bundle.getInt(getString(R.string.article_display_mode),-1);

        ApiClient acOne, acTwo;
        acOne = new ApiClient(getContext(), GET_ARTICLE,this,this);

        switch (chosenDisplay){
            case DISPLAY_PURCHASE:
                acTwo = new ApiClient(getContext(), GET_PURCHASE,this,this);
                acTwo.getPurchase(receivedPurchaseId);

            case DISPLAY_ONLY:
                fab.setVisibility(View.GONE);

            case DISPLAY_WITH_BUY:
                acOne.getArticle(receivedArticleId);

                break;


        }

        return rootView;
    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        switch (requestCode){
            case GET_ARTICLE:
                loadExistingArticle(data);
                break;

            case POST_PURCHASE:
                Toast.makeText(getContext(),getString(R.string.msg_successful_purchased),Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;

            case GET_PURCHASE:
                Purchase purchase = (Purchase) data;
                String status = purchase.isCompleted() ? getString(R.string.status_done): getString(R.string.status_open);
                String[] texts = new String[]{status, purchase.getDate()};

                loadPurchaseInfo(texts);

                break;
        }
    }

    @Override
    public void onFailure(String msg, int requestCode) {
                Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
                getActivity().finish();
    }

    private void loadExistingArticle(Object data){
        currentArticle = (Article) data;
        getActivity().setTitle(currentArticle.getName());

        TextInputEditText etName, etDescription, etPrice, etCondition, etCreatedAt, etId,
                etExtra1, etExtra2, etExtra3 ;

        TextInputLayout ilExtra1, ilExtra2, ilExtra3;

        etName = (TextInputEditText) rootView.findViewById(R.id.article_name);
        etDescription = (TextInputEditText) rootView.findViewById(R.id.article_description);
        etPrice = (TextInputEditText) rootView.findViewById(R.id.article_price);
        etCondition = (TextInputEditText) rootView.findViewById(R.id.article_condition);
        etCreatedAt = (TextInputEditText) rootView.findViewById(R.id.article_created_at);
        etId = (TextInputEditText) rootView.findViewById(R.id.article_id);

        TextInputEditText[] basicViews = new TextInputEditText[]{etDescription, etPrice, etCondition, etCreatedAt, etId};

        etName.setVisibility(View.GONE);
        setDisabled(basicViews);

        String[] basicTexts = new String[]{currentArticle.getDescription(), currentArticle.getPrice(), currentArticle.getCondition(), currentArticle.getCreatedAt(), ""+currentArticle.getId() };
        setText(basicViews, basicTexts);

        TextInputEditText[] extraViews;
        TextInputLayout[] extraHintsLayouts;
        String[] hints, extraTexts;

        switch (type){
            case BOOK:
                Book book = (Book) data;

                etExtra1 = (TextInputEditText) rootView.findViewById(R.id.article_extra1);
                etExtra2 = (TextInputEditText) rootView.findViewById(R.id.article_extra2);
                etExtra3 = (TextInputEditText) rootView.findViewById(R.id.article_extra3);

                ilExtra1 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra1);
                ilExtra2 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra2);
                ilExtra3 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra3);

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
                Electronic electronic = (Electronic) data;

                etExtra1 = (TextInputEditText) rootView.findViewById(R.id.article_extra1);
                etExtra2 = (TextInputEditText) rootView.findViewById(R.id.article_extra2);

                ilExtra1 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra1);
                ilExtra2 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra2);

                extraViews = new TextInputEditText[]{etExtra1, etExtra2};
                extraHintsLayouts = new TextInputLayout[]{ilExtra1, ilExtra2};

                setVisible(extraViews);
                setDisabled(extraViews);

                hints = new String[]{getString(R.string.article_producer), getString(R.string.article_model)};
                setHint(extraHintsLayouts, hints);

                extraTexts = new String[]{electronic.getProducer(), electronic.getModel()};
                setText(extraViews,extraTexts);

                break;
        }
    }

    private void loadPurchaseInfo(String[] texts){
        TextInputEditText etStatus, etPurchaseDate;

        etStatus = (TextInputEditText) rootView.findViewById(R.id.article_status);
        etPurchaseDate = (TextInputEditText) rootView.findViewById(R.id.article_purchase_date);

        TextInputEditText[] purchaseViews = new TextInputEditText[]{etStatus,etPurchaseDate};

        setVisible(purchaseViews);
        setDisabled(purchaseViews);

        setText(purchaseViews,texts);
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

                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_credentials), Context.MODE_PRIVATE);
                String accountJson = sharedPref.getString(getString(R.string.secret_account),"");

                if(accountJson.length() != 0){
                    Account account = Account.makeAccount(accountJson);
                    fab.setEnabled(false);

                    ApiClient apiClient = new ApiClient(getContext(), POST_PURCHASE, this, this);
                    apiClient.postPurchase(currentArticle, account);
                }else{
                    Toast.makeText(getContext(),getString(R.string.msg_login_first),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }

                break;
        }

    }
}
