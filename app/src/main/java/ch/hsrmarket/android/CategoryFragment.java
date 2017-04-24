package ch.hsrmarket.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.hsrmarket.android.adapter.CategoryAdapter;
import ch.hsrmarket.android.api.ApiClient;
import ch.hsrmarket.android.model.Article;
import ch.hsrmarket.android.model.Book;
import ch.hsrmarket.android.model.ElectronicDevice;
import ch.hsrmarket.android.model.OfficeSupply;
import ch.hsrmarket.android.model.Other;


public class CategoryFragment extends Fragment implements ApiClient.OnResponseListener{

    private CategoryAdapter adapter;
    private Article.Type appointedCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        Bundle bundle = getArguments();
        appointedCategory = (Article.Type) bundle.getSerializable("appointedCategory");

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.listCategory);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new CategoryAdapter<>();
        recyclerView.setAdapter(adapter);

        ApiClient apiClient = new ApiClient();
        apiClient.setOnResponseListener(this);

        switch (appointedCategory){
            case BOOK:
                apiClient.requestBooks();
                break;
            case ELECTRONIC_DEVICE:
                apiClient.requestElectronicDevices();
                break;
            case OFFICE_SUPPLY:
                apiClient.requestOfficeSupplies();
                break;
            case OTHER:
                apiClient.requestOther();
                break;
        }

        return root;
    }

    @Override
    public void onDataLoaded(Object data) {

        switch (appointedCategory){
            case BOOK:
                List<Book> books = (List<Book>) data;
                adapter.setData(books);
                break;
            case ELECTRONIC_DEVICE:
                List<ElectronicDevice> electronicDevices = (List<ElectronicDevice>) data;
                adapter.setData(electronicDevices);
                break;
            case OFFICE_SUPPLY:
                List<OfficeSupply> officeSupplies = (List<OfficeSupply>) data;
                adapter.setData(officeSupplies);
                break;
            case OTHER:
                List<Other> others = (List<Other>) data;
                adapter.setData(others);
                break;
        }
    }
}

