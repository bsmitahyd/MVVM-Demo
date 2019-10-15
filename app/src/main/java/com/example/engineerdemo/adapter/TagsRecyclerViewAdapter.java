package com.example.engineerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engineerdemo.MainActivity;
import com.example.engineerdemo.R;
import com.example.engineerdemo.model.DataModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TagsRecyclerViewAdapter extends RecyclerView.Adapter<TagsRecyclerViewAdapter.TagsViewHolder> {
    private Context context;
    private List<DataModel.HitList> hitLists;
    boolean isLoad = false;
    MainActivity activity;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false;
    private boolean enableDisableSwitch;
    private OnLoadMoreListener delegate;

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
    }

    @Override
    public int getItemCount() {
        return hitLists.size();
    }

    public class TagsViewHolder extends RecyclerView.ViewHolder {
        TextView tagsTextView;
        TextView dateTextView;

        public TagsViewHolder(@NonNull View itemView) {
            super(itemView);
            tagsTextView = itemView.findViewById(R.id.tvTitle);
            dateTextView = itemView.findViewById(R.id.tvObject);
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
