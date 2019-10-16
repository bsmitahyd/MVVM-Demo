package com.example.engineerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engineerdemo.MainActivity;
import com.example.engineerdemo.R;
import com.example.engineerdemo.interfaces.OnPostClickedInterface;
import com.example.engineerdemo.model.DataModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TagsRecyclerViewAdapter extends RecyclerView.Adapter<TagsRecyclerViewAdapter.TagsViewHolder> {
    private Context context;
    private List<DataModel.HitList> hitLists;
    boolean isLoad = false;
    MainActivity activity;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false;
    private boolean enableDisableSwitch;
    public OnPostClickedInterface delegate;

    public TagsRecyclerViewAdapter(Context context, List<DataModel.HitList> hitLists) {
        this.context = context;
        this.hitLists = hitLists;
        activity = (MainActivity) context;
    }

    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_row, parent, false);
        return new TagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        final DataModel.HitList list = hitLists.get(position);
        holder.tagsTextView.setText(list.getTitle());
        holder.dateTextView.setText(changeDateFormate(list.getCreated_at()));
        if (hitLists.get(position).isSelected()) {
            holder.aSwitch.setChecked(true);

        } else {
            holder.aSwitch.setChecked(false);
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setSelected(!list.isSelected());
                notifyItemChanged(holder.getAdapterPosition(), list);
                delegate.OnPostClicked(list);

            }
        });
    }

    @Override
    public int getItemCount() {
        return hitLists.size();
    }

    public class TagsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.simpleSwitch)
        Switch aSwitch;
        @BindView(R.id.tvTitle)
        TextView tagsTextView;
        @BindView(R.id.tvObject)
        TextView dateTextView;
        @BindView(R.id.cdMain)
        CardView mCardView;

        public TagsViewHolder(@NonNull View itemView) {
            super(itemView);
            Unbinder unbinder = ButterKnife.bind(this, itemView);
        }
    }

    private String changeDateFormate(String date) {
        String changedDate = "";
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

            Date date1 = input.parse(date);
            changedDate = output.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Created at : " + changedDate;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
