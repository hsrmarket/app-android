package ch.hsrmarket.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ch.hsrmarket.android.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView name, price, description;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.item_name);
        price = (TextView) itemView.findViewById(R.id.item_price);
        description = (TextView) itemView.findViewById(R.id.item_description);
    }
}
