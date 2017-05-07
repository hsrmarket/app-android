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

import ch.hsrmarket.android.adapter.CategoryAdapter;
import ch.hsrmarket.android.adapter.EmptyAdapter;
import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Article;


public class CategoryFragment extends Fragment implements ApiClient.OnResponseListener, ApiClient.OnFailureListener, CategoryAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private Article.Type appointedCategory;

    public static final int CATEGORY_ITEMS_REQUEST = 3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        Bundle bundle = getArguments();
        appointedCategory = (Article.Type) bundle.getSerializable(getString(R.string.appointed_category));

        recyclerView = (RecyclerView) root.findViewById(R.id.listCategory);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        ApiClient apiClient = new ApiClient(getContext(), CATEGORY_ITEMS_REQUEST, this, this);
        EmptyAdapter adapter = new EmptyAdapter();
        recyclerView.setAdapter(adapter);
        apiClient.requestCategoryList(appointedCategory);
    }

    @Override
    public void onDataLoaded(Object data, int requestCode) {
        if(requestCode == CATEGORY_ITEMS_REQUEST){

            List<Article> items = (List<Article>) data;

            if(items.isEmpty()){
                EmptyAdapter adapter = new EmptyAdapter(getString(R.string.msg_empty),R.drawable.ic_empty);
                recyclerView.setAdapter(adapter);
            }else {
                CategoryAdapter adapter = new CategoryAdapter(items);
                adapter.setOnItemClickListener(this);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onFailure(String msg, int requestCode) {
        if(requestCode == CATEGORY_ITEMS_REQUEST){
            EmptyAdapter adapter = new EmptyAdapter(msg,R.drawable.ic_warning);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Integer id = (Integer) view.getTag();

        Intent intent = new Intent(getContext(),ArticleActivity.class);
        intent.putExtra(getString(R.string.article_pass_id),id);
        intent.putExtra(getString(R.string.article_pass_type),appointedCategory);

        startActivity(intent);
    }
}

