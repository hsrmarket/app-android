package ch.hsrmarket.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.hsrmarket.android.adapter.ListAdapter;
import ch.hsrmarket.android.adapter.EmptyAdapter;
import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Article;


public class ListFragment extends Fragment implements ApiClient.OnResponseListener, ApiClient.OnFailureListener, ListAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private Article.Type appointedCategory;
    private int appointedMyList;
    private int accountId;
    private int requestOrigin;
    private List<Article> items;

    public static final int ORIGIN_CATEGORY = 5;
    public static final int ORIGIN_MY_ARTICLES = 7;
    public static final int ORIGIN_MY_PURCHASES = 11;
    public static final int ORIGIN_MY_SALES = 13;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        Bundle bundle = getArguments();
        appointedCategory = (Article.Type) bundle.getSerializable(getString(R.string.appointed_category));
        appointedMyList = bundle.getInt(getString(R.string.appointed_mylist),-1);
        accountId = bundle.getInt(getString(R.string.mylist_account_id),-1);
        requestOrigin = bundle.getInt(getString(R.string.request_origin),-1);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        ApiClient apiClient = new ApiClient(getContext(), 0, this, this);
        EmptyAdapter adapter = new EmptyAdapter();
        recyclerView.setAdapter(adapter);

        switch (requestOrigin){
            case ORIGIN_CATEGORY:
                apiClient.getArticleList(appointedCategory);
                break;
            case ORIGIN_MY_ARTICLES:
                apiClient.getArticleList(accountId);
                break;
            case ORIGIN_MY_PURCHASES:
            case ORIGIN_MY_SALES:
                apiClient.getArticleList(accountId,appointedMyList);
                break;
        }
    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        items = (List<Article>) data;

        if(items.isEmpty()){
            EmptyAdapter adapter = new EmptyAdapter(getString(R.string.msg_listview_empty),R.drawable.ic_empty);
            recyclerView.setAdapter(adapter);
        }else {
            ListAdapter adapter = new ListAdapter(items);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onFailure(String msg, int requestCode) {
            EmptyAdapter adapter = new EmptyAdapter(msg,R.drawable.ic_warning);
            recyclerView.setAdapter(adapter);
   }

    @Override
    public void onClick(View view, int position) {
        Article article = items.get(position);

        Intent intent = new Intent(getContext(),ArticleActivity.class);
        intent.putExtra(getString(R.string.article_pass_id),article.getId());
        intent.putExtra(getString(R.string.article_pass_type),article.getType());
        intent.putExtra(getString(R.string.article_pass_purchase_id),article.getPurchaseId());

        switch (requestOrigin){
            case ORIGIN_CATEGORY:
                intent.putExtra(getString(R.string.article_display_mode),ArticleActivity.DISPLAY_WITH_BUY);
                break;
            case ORIGIN_MY_ARTICLES:
                intent.putExtra(getString(R.string.article_display_mode),ArticleActivity.DISPLAY_ONLY);
                break;
            case ORIGIN_MY_PURCHASES:
                intent.putExtra(getString(R.string.pass_account_id),article.getSellerId());
                intent.putExtra(getString(R.string.article_display_mode),ArticleActivity.DISPLAY_PURCHASE);
                break;
            case ORIGIN_MY_SALES:
                intent.putExtra(getString(R.string.pass_account_id),article.getBuyerId());
                intent.putExtra(getString(R.string.article_display_mode),ArticleActivity.DISPLAY_SALE);
                break;
        }

        startActivity(intent);
    }
}