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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Article;
import ch.hsrmarket.android.model.Book;
import ch.hsrmarket.android.model.Electronic;
import ch.hsrmarket.android.model.OfficeSupply;
import ch.hsrmarket.android.model.Other;
import ch.hsrmarket.android.model.Purchase;

import static ch.hsrmarket.android.ArticleActivity.DISPLAY_ADD;
import static ch.hsrmarket.android.ArticleActivity.DISPLAY_ONLY;
import static ch.hsrmarket.android.ArticleActivity.DISPLAY_PURCHASE;
import static ch.hsrmarket.android.ArticleActivity.DISPLAY_WITH_BUY;

public class ArticleFragment extends Fragment implements ApiClient.OnResponseListener, ApiClient.OnFailureListener, View.OnClickListener, Spinner.OnItemSelectedListener {

    TextInputEditText etName, etDescription, etPrice, etCondition, etCreatedAt, etId;
    TextInputEditText etExtra1, etExtra2, etExtra3;

    private FloatingActionButton fab;
    private Article.Type type;
    private Article currentArticle;

    private int receivedArticleId;
    private int receivedPurchaseId;
    private int chosenDisplay;

    private View rootView;

    public static final int GET_ARTICLE = 1;
    public static final int POST_PURCHASE = 2;
    public static final int GET_PURCHASE = 3;
    public static final int PATCH_PURCHASE = 4;
    public static final int POST_ARTICLE = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_article, container, false);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_buy);
        fab.setOnClickListener(this);

        Bundle bundle = getArguments();
        receivedArticleId = bundle.getInt(getString(R.string.article_pass_id),-1);
        type = (Article.Type) bundle.getSerializable(getString(R.string.article_pass_type));
        receivedPurchaseId = bundle.getInt(getString(R.string.article_pass_purchase_id),-1);

        chosenDisplay = bundle.getInt(getString(R.string.article_display_mode),-1);

        etName = (TextInputEditText) rootView.findViewById(R.id.article_name);
        etDescription = (TextInputEditText) rootView.findViewById(R.id.article_description);
        etPrice = (TextInputEditText) rootView.findViewById(R.id.article_price);
        etCondition = (TextInputEditText) rootView.findViewById(R.id.article_condition);
        etCreatedAt = (TextInputEditText) rootView.findViewById(R.id.article_created_at);
        etId = (TextInputEditText) rootView.findViewById(R.id.article_id);

        etExtra1 = (TextInputEditText) rootView.findViewById(R.id.article_extra1);
        etExtra2 = (TextInputEditText) rootView.findViewById(R.id.article_extra2);
        etExtra3 = (TextInputEditText) rootView.findViewById(R.id.article_extra3);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ApiClient acOne, acTwo;

        switch (chosenDisplay){
            case DISPLAY_PURCHASE:
                fab.setImageResource(R.drawable.ic_done);

                acOne = new ApiClient(getContext(), GET_ARTICLE,this,this);
                acOne.getArticle(receivedArticleId);

                acTwo = new ApiClient(getContext(), GET_PURCHASE,this,this);
                acTwo.getPurchase(receivedPurchaseId);

                break;

            case DISPLAY_ONLY:
                fab.setVisibility(View.GONE);

                acOne = new ApiClient(getContext(), GET_ARTICLE,this,this);
                acOne.getArticle(receivedArticleId);

                break;

            case DISPLAY_WITH_BUY:
                acOne = new ApiClient(getContext(), GET_ARTICLE,this,this);
                acOne.getArticle(receivedArticleId);

                break;

            case DISPLAY_ADD:
                fab.setImageResource(R.drawable.ic_tick);
                getActivity().setTitle(R.string.title_adding_article);

                Spinner spinner = (Spinner) rootView.findViewById(R.id.article_type);
                TextView hintType = (TextView) rootView.findViewById(R.id.article_hint_type);

                View[] addingViews = new View[]{hintType,spinner};
                setVisible(addingViews);

                TextInputEditText etCreatedAt = (TextInputEditText) rootView.findViewById(R.id.article_created_at);
                TextInputEditText etId = (TextInputEditText) rootView.findViewById(R.id.article_id);

                View[] disappearingViews = new View[]{etCreatedAt, etId};
                setGone(disappearingViews);

                spinner.setOnItemSelectedListener(this);

                break;
        }

    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        switch (requestCode){
            case GET_ARTICLE:
                loadExistingArticle(data);
                break;

            case POST_PURCHASE:
                Toast.makeText(getContext(),getString(R.string.msg_successful_purchased),Toast.LENGTH_SHORT).show();
                fab.setEnabled(true);
                getActivity().finish();
                break;

            case GET_PURCHASE:
                Purchase purchase = (Purchase) data;
                String status = purchase.isCompleted() ? getString(R.string.status_done): getString(R.string.status_open);
                String[] texts = new String[]{status, purchase.getDate()};

                if(purchase.isCompleted()){
                    fab.setVisibility(View.GONE);
                }

                loadPurchaseInfo(texts);
                break;

            case PATCH_PURCHASE:
                onStart();

                break;

            case POST_ARTICLE:
                Toast.makeText(getContext(),getString(R.string.msg_successful_new_article),Toast.LENGTH_SHORT).show();
                getActivity().finish();

                break;
        }

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.article_base_layout);
        linearLayout.requestFocus();
    }

    @Override
    public void onFailure(String msg, int requestCode) {
                Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
                fab.setEnabled(true);
                getActivity().finish();
    }

    private void loadExistingArticle(Object data){
        currentArticle = (Article) data;
        getActivity().setTitle(currentArticle.getName());

        TextInputEditText[] basicViews = new TextInputEditText[]{etDescription, etPrice, etCondition, etCreatedAt, etId};

        etName.setVisibility(View.GONE);
        setDisabled(basicViews);

        String[] basicTexts = new String[]{currentArticle.getDescription(), currentArticle.getPrice(), currentArticle.getCondition(), currentArticle.getCreatedAt(), ""+currentArticle.getId() };
        setText(basicViews, basicTexts);


        switch (type){
            case BOOK:
                Book book = (Book) data;
                showBookViews(true, book.getAuthor(),book.getPublisher(),book.getISBN());

                break;

            case ELECTRONIC_DEVICE:
                Electronic electronic = (Electronic) data;
                showElectronicViews(true, electronic.getProducer(), electronic.getModel());

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

    private void showBookViews(Boolean disabled, String... args){
        TextInputLayout ilExtra1, ilExtra2, ilExtra3;

        ilExtra1 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra1);
        ilExtra2 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra2);
        ilExtra3 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra3);

        TextInputEditText[] extraViews = new TextInputEditText[]{etExtra1, etExtra2, etExtra3};
        TextInputLayout[] extraHintsLayouts = new TextInputLayout[]{ilExtra1, ilExtra2, ilExtra3};

        setVisible(extraViews);

        if(disabled) {
            setDisabled(extraViews);
        }

        String[] hints = new String[]{getString(R.string.article_author), getString(R.string.article_publisher), getString(R.string.article_isbn)};
        setHint(extraHintsLayouts, hints);

        if(args.length == 3) {
            setText(extraViews, args);
        }

    }

    private void showElectronicViews(Boolean disabled, String... args){
        TextInputLayout ilExtra1, ilExtra2;

        ilExtra1 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra1);
        ilExtra2 = (TextInputLayout) rootView.findViewById(R.id.article_hint_extra2);

        TextInputEditText[] extraViews = new TextInputEditText[]{etExtra1, etExtra2};
        TextInputLayout[] extraHintsLayouts = new TextInputLayout[]{ilExtra1, ilExtra2};

        setVisible(extraViews);

        if(disabled){
            setDisabled(extraViews);
        }

        String[] hints = new String[]{getString(R.string.article_producer), getString(R.string.article_model)};
        setHint(extraHintsLayouts, hints);

        if(args.length == 2) {
            setText(extraViews, args);
        }
    }

    private void disappearExtraViews(){
        TextInputEditText[] extraViews = new TextInputEditText[]{etExtra1, etExtra2, etExtra3};
        setGone(extraViews);
    }

    private boolean isNewArticleValid(){
        EditText[] basicViews = new EditText[]{etName, etDescription, etPrice, etCondition};
        EditText[] bookViews = new EditText[]{etExtra1, etExtra2, etExtra3};
        EditText[] electronicViews = new EditText[]{etExtra1, etExtra2};

        switch (type){
            case BOOK:
                return !isEmpty(basicViews) && !isEmpty(bookViews);

            case ELECTRONIC_DEVICE:
                return !isEmpty(basicViews) && !isEmpty(electronicViews);

            default:
                return !isEmpty(basicViews);
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

    private void setGone(View[] views){
        for(View v: views){
            v.setVisibility(View.GONE);
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

    private String getString(EditText editText){
        return editText.getText().toString();
    }

    private int getInt(EditText editText){
        return Integer.parseInt(editText.getText().toString());
    }

    private double getDouble(EditText editText){
        return Double.parseDouble(editText.getText().toString());
    }

    private Object getNewArticle(){
        switch (type){
            case BOOK:
                return new Book(getString(etName),getDouble(etPrice),getInt(etCondition),getString(etDescription),getString(etExtra3),getString(etExtra1),getString(etExtra2));

            case ELECTRONIC_DEVICE:
                return new Electronic(getString(etName),getDouble(etPrice),getInt(etCondition),getString(etDescription),getString(etExtra1),getString(etExtra2));

            case OFFICE_SUPPLY:
                return new OfficeSupply(getString(etName),getDouble(etPrice),getInt(etCondition),getString(etDescription));

            case OTHER:
                return new Other(getString(etName),getDouble(etPrice),getInt(etCondition),getString(etDescription));

            default:
                throw new AssertionError("Forgot to implement");
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_credentials), Context.MODE_PRIVATE);
        String accountJson = sharedPref.getString(getString(R.string.secret_account),"");

        if(accountJson.length() != 0){

            ApiClient apiClient;
            Account account = Account.makeAccount(accountJson);

            switch (chosenDisplay){
                case DISPLAY_WITH_BUY:

                    fab.setEnabled(false);

                    apiClient = new ApiClient(getContext(), POST_PURCHASE, this, this);
                    apiClient.postPurchase(currentArticle, account);

                    break;

                case DISPLAY_PURCHASE:
                    apiClient = new ApiClient(getContext(), PATCH_PURCHASE, this, this);
                    apiClient.patchPurchaseCompleted(receivedPurchaseId);

                    break;

                case DISPLAY_ADD:
                    if(isNewArticleValid()){
                        Article newArticle = (Article) getNewArticle();

                        apiClient = new ApiClient(getContext(),POST_ARTICLE,this,this);
                        apiClient.postArticle(account.getId(), newArticle);
                    }
            }


        }else{
            Toast.makeText(getContext(),getString(R.string.msg_login_first),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(),LoginActivity.class));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                type = Article.Type.BOOK;
                showBookViews(false);
                break;

            case 1:
                type = Article.Type.ELECTRONIC_DEVICE;
                disappearExtraViews();
                showElectronicViews(false);
                break;

            case 2:
                type = Article.Type.OFFICE_SUPPLY;
                disappearExtraViews();
                break;

            case 3:
                type = Article.Type.OTHER;
                disappearExtraViews();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
