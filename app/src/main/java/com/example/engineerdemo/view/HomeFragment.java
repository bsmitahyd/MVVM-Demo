package com.example.engineerdemo.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engineerdemo.MainActivity;
import com.example.engineerdemo.R;
import com.example.engineerdemo.adapter.TagsRecyclerViewAdapter;
import com.example.engineerdemo.model.DataModel;
import com.example.engineerdemo.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    private MainActivity activity;
    private TagsRecyclerViewAdapter tagsRecyclerViewAdapter;
    private DataViewModel dataViewModel;
    private List<DataModel.HitList> hitLists;
    private RecyclerView.LayoutManager layoutManager;
    private int api_in_progress = 0;
    int pagenumber = 1;
    private Unbinder unbinder;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        hitLists = new ArrayList<>();
        //bindList(hitLists);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);


        dataViewModel = ViewModelProviders.of(activity).get(DataViewModel.class);
        dataViewModel.getTagsData();
        dataViewModel.getDataModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<DataModel>() {
            @Override
            public void onChanged(DataModel dataModel) {
                if (dataModel.getHits() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    api_in_progress = 0;
                    recyclerView.setVisibility(View.VISIBLE);

                    hitLists.addAll(dataModel.getHits());
                    tagsRecyclerViewAdapter = new TagsRecyclerViewAdapter(activity, hitLists);
                    recyclerView.setAdapter(tagsRecyclerViewAdapter);
                    bindList(hitLists);
                }
            }
        });
    }

    private void bindList(List<DataModel.HitList> hitLists) {
        activity.toolbar.setTitle("Total displaying posts = " + hitLists.size());

        tagsRecyclerViewAdapter.getItemCount();
        tagsRecyclerViewAdapter.setLoadMoreListener(new TagsRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hitLists.size() > 0) {
                            progressBar.setVisibility(View.VISIBLE);
                            loadMore();
                        }
                    }
                });
            }
        });
    }

    private void loadMore() {
        api_in_progress = 1;
        pagenumber++;
        dataViewModel.getTagsData();

        tagsRecyclerViewAdapter.setLoadMoreListener(new TagsRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hitLists.size() > 0) {
                            progressBar.setVisibility(View.GONE);
                            loadMore();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
