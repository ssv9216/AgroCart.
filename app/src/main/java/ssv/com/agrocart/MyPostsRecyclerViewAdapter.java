package ssv.com.agrocart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyPostsRecyclerViewAdapter extends RecyclerView.Adapter<MyPostsRecyclerViewAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private OnItemClickListener mListener;

    private Context mContext;
    private List<MyPostsGetterSetter> mCrop;
    private DatabaseReference reff;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public MyPostsRecyclerViewAdapter(Context context, List<MyPostsGetterSetter> crop) {
        mContext = (Context) context;
        mCrop = crop;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_posts_sample, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MyPostsGetterSetter cropCurrent = mCrop.get(position);
        holder.name.setText(cropCurrent.getName());
        holder.price.setText(cropCurrent.getPrice());
        holder.weight.setText(cropCurrent.getWeight());



        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.weight);
                popupMenu.inflate(R.menu.delete_menu);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.delete_item:

                                mCrop.remove(position);
                                notifyDataSetChanged();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCrop.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView price;
        TextView weight;
        CardView parentLayout;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCropNameMyPosts);
            price = itemView.findViewById(R.id.tvCropPriceMyPosts);
            weight = itemView.findViewById(R.id.tvCropWeightMyPosts);
            parentLayout = itemView.findViewById(R.id.parent_layout_my_posts);

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
