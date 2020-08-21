package ssv.com.agrocart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private OnItemClickListener mListener;
    private Context mContext;
    private ArrayList<NewsItem> mNewsList;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(NewsAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public NewsAdapter(Context context, ArrayList<NewsItem> newsItem){
        mContext = context;
        mNewsList = newsItem;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent,false);
        return new NewsViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        final NewsItem currentItem = mNewsList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String title = currentItem.getTitle();
        String desc = currentItem.getDesc();
        String url = currentItem.getUrl();


        holder.mTextViewTitle.setText(title);
        holder.mTextViewDesc.setText(desc);
        Picasso.with(mContext).load(imageUrl).fit().
                centerCrop().into(holder.mImageView);



    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewDesc;


        public NewsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewTitle = itemView.findViewById(R.id.tvTitle);
            mTextViewDesc= itemView.findViewById(R.id.tvDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
