package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ch.hsrmarket.android.adapter.CategoryAdapter;
import ch.hsrmarket.android.adapter.EmptyAdapter;
import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Article;


public class CategoryFragment extends Fragment implements ApiClient.OnResponseListener, ApiClient.OnFailureListener, CategoryAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private Article.Type appointedCategory;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        Bundle bundle = getArguments();
        appointedCategory = (Article.Type) bundle.getSerializable(getString(R.string.appointed_category));

        recyclerView = (RecyclerView) root.findViewById(R.id.listCategory);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        ApiClient apiClient = new ApiClient();
        apiClient.setOnResponseListener(this);
        apiClient.setOnFailureListener(this);

        if(isOnline()){
            apiClient.requestCategoryList(appointedCategory);
        }else {
            EmptyAdapter adapter = new EmptyAdapter(getString(R.string.error_no_internet));
            recyclerView.setAdapter(adapter);
        }

        return root;
    }

    @Override
    public void onDataLoaded(Object data) {
        List<Article> items = (List<Article>) data;
        CategoryAdapter adapter = new CategoryAdapter(items);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure(String msg) {
        EmptyAdapter adapter = new EmptyAdapter(msg);
        recyclerView.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View view, int position) {
        Integer id = (Integer) view.getTag();

        Intent intent = new Intent(getContext(),ArticleActivity.class);
        intent.putExtra(getString(R.string.article_pass_id),id);
        intent.putExtra(getString(R.string.article_pass_type),appointedCategory);

        if(isOnline()){
            startActivity(intent);
        }
    }
}

