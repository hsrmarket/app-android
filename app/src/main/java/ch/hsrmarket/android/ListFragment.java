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

    public static final int ORIGIN_MY_LIST = 3;
    public static final int ORIGIN_CATEGORY = 5;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        Bundle bundle = getArguments();
        appointedCategory = (Article.Type) bundle.getSerializable(getString(R.string.appointed_category));
        appointedMyList = bundle.getInt(getString(R.string.appointed_mylist),-1);
        accountId = bundle.getInt(getString(R.string.account_pass_id),-1);
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
                apiClient.requestCategoryList(appointedCategory);
                break;
            case ORIGIN_MY_LIST:
                apiClient.getMyList(accountId,appointedMyList);
                break;
        }
    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        List<Article> items = (List<Article>) data;

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
        Integer id = (Integer) view.getTag(R.integer.article_id);
        Article.Type type = (Article.Type) view.getTag(R.integer.article_type);

        Intent intent = new Intent(getContext(),ArticleActivity.class);
        intent.putExtra(getString(R.string.article_pass_id),id);
        intent.putExtra(getString(R.string.article_pass_type),type);

        switch (requestOrigin){
            case ORIGIN_CATEGORY:
                intent.putExtra(getString(R.string.article_display_mode),ArticleActivity.DISPLAY_WITH_BUY);
                break;
            case ORIGIN_MY_LIST:
                intent.putExtra(getString(R.string.article_display_mode),ArticleActivity.DISPLAY_ONLY);
                break;
        }

        startActivity(intent);
    }
}

