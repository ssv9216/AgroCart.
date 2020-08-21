package ssv.com.agrocart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpertGuideAdapter extends RecyclerView.Adapter<ExpertGuideAdapter.ViewHolder> {

    Context mContext;
    List<ExpertGuideItems> mGuides;

    ExpertGuideAdapter(Context context, List<ExpertGuideItems> guides){
        mContext = context;
        mGuides = guides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expert_guide_sample, parent, false);
        ViewHolder v = new ViewHolder(view);
        return  v;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpertGuideItems itemCurrent  = mGuides.get(position);
        holder.auName.setText(itemCurrent.getName());
        holder.guide.setText(itemCurrent.getGuide());
    }

    @Override
    public int getItemCount() {
        return mGuides.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView auName, guide;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            auName = itemView.findViewById(R.id.tvAuthorName);
            guide = itemView.findViewById(R.id.tvGuide);

        }
    }
}
