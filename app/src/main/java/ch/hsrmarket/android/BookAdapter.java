package ch.hsrmarket.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by athanabalasingam on 05.04.17.
 */

public class BookAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Book> books;
    private int rowLayout;
    private Context context;

    public BookAdapter(List<Book> books, int rowLayout, Context context){
        this.books = books;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        TextView tvId = (TextView) view.findViewById(R.id.tv_id);
        TextView tvIsbn = (TextView) view.findViewById(R.id.tv_isbn);
        TextView tvAuthor = (TextView) view.findViewById(R.id.tv_author);

        return new ViewHolder(view, tvId, tvIsbn, tvAuthor);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Book b = books.get(position);
            holder.tvId.setText(""+b.getId());
            holder.tvAuthor.setText(b.getAuthor());
            holder.tvIsbn.setText(b.getISBN());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


}
