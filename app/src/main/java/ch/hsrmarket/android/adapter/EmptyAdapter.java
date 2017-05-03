package ch.hsrmarket.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.hsrmarket.android.R;

public class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder> {
    private String msgText;
    private Integer imageUrl;

    public EmptyAdapter(){}

    public EmptyAdapter(String message){
        msgText = message;
    }

    public EmptyAdapter(int imageUrl){
        this.imageUrl = imageUrl;
    }

    public EmptyAdapter(String message, int imageUrl){
        msgText = message;
        this.imageUrl = imageUrl;
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

        if(imageUrl != null){
            holder.imageView.setImageResource(imageUrl);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg;
        public ImageView imageView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.empty_msg);
            imageView = (ImageView) itemView.findViewById(R.id.empty_image);
        }
    }

}
