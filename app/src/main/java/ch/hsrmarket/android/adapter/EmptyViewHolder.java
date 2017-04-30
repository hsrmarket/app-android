package ch.hsrmarket.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ch.hsrmarket.android.R;

public class EmptyViewHolder extends RecyclerView.ViewHolder {
    public TextView msg;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        msg = (TextView) itemView.findViewById(R.id.empty_msg);
    }
}
