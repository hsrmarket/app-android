package ch.hsrmarket.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.hsrmarket.android.R;
import ch.hsrmarket.android.model.Article;

public class CategoryAdapter<T extends Article> extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<T> data;

    public CategoryAdapter(List<T> data){
        this.data = data;
    }

    public CategoryAdapter(){

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Article article = data.get(position);

        holder.name.setText(article.getName());
        holder.price.setText(""+article.getPrice());
        holder.description.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
