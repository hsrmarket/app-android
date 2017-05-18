package ch.hsrmarket.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.hsrmarket.android.R;
import ch.hsrmarket.android.model.Article;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnItemClickListener{
        public void onClick(View view, int position);
    }

    private List<Article> data;
    private OnItemClickListener clickListener;

    public CategoryAdapter(List<Article> data){
        this.data = data;
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

        holder.root.setTag(R.integer.article_id,article.getId());
        holder.root.setTag(R.integer.article_type,article.getType());

        holder.name.setText(article.getName());
        holder.price.setText( article.getPrice());
        holder.description.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price, description;
        public View root;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            root = itemView;
            name = (TextView) itemView.findViewById(R.id.item_name);
            price = (TextView) itemView.findViewById(R.id.item_price);
            description = (TextView) itemView.findViewById(R.id.item_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.onClick(v,getAdapterPosition());
            }
        }
    }
}

