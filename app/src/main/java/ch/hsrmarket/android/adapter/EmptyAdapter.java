package ch.hsrmarket.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsrmarket.android.R;

public class EmptyAdapter extends RecyclerView.Adapter<EmptyViewHolder> {
    private String msgText;

    public EmptyAdapter(){}

    public EmptyAdapter(String message){
        msgText = message;
    }

    @Override
    public EmptyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_item, parent, false);
        return new EmptyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmptyViewHolder holder, int position) {
        if(msgText != null){
            holder.msg.setText(msgText);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
