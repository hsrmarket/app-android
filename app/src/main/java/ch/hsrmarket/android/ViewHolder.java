package ch.hsrmarket.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by athanabalasingam on 05.04.17.
 */

public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView tvId;
    public TextView tvIsbn;
    public TextView tvAuthor;

    public ViewHolder(View v, TextView tvId, TextView tvIsbn , TextView tvAuthor) {
        super(v);

        this.tvId = tvId;
        this.tvIsbn = tvIsbn;
        this.tvAuthor = tvAuthor;
    }
}