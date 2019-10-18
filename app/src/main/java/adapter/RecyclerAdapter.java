package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.zip.Inflater;

import jdo.Books;
import sample.activity.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Books> mBooksArrayList;

    public RecyclerAdapter(Context mContext, ArrayList<Books> lBooksArrayList) {
        this.mContext = mContext;
        this.mBooksArrayList = lBooksArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_activity,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titletextview.setText(mBooksArrayList.get(position).getmTitle());
        Glide.with(mContext).load(R.drawable.books).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mBooksArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titletextview;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titletextview = itemView.findViewById(R.id.title);
            imageView=itemView.findViewById(R.id.image);
        }
    }
}
