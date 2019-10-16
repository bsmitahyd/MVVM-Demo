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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.engineerdemo.MainActivity;
import com.example.engineerdemo.R;
import com.example.engineerdemo.adapter.TagsRecyclerViewAdapter;
import com.example.engineerdemo.interfaces.OnPostClickedInterface;
import com.example.engineerdemo.model.DataModel;
import com.example.engineerdemo.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnPostClickedInterface {

    private MainActivity activity;
    private TagsRecyclerViewAdapter tagsRecyclerViewAdapter;
    private DataViewModel dataViewModel;
    private List<DataModel.HitList> hitLists;
    private int api_in_progress = 0;
    private int pagenumber = 0;
    private int count = 0;
    private Unbinder unbinder;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);


        dataViewModel = ViewModelProviders.of(activity).get(DataViewModel.class);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        dataViewModel.getTagsData();
        dataViewModel.getDataModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<DataModel>() {
            @Override
            public void onChanged(DataModel dataModel) {
                swipeRefreshLayout.setRefreshing(false);//TODO to make visible gone swipe to refresh loading icon
                if (dataModel != null && dataModel.getHits() != null) {
                    //TODO - below (pagenumber == 1)is the first time api call or swipe to refresh
                    if (pagenumber == 1) {
                        count = 0;
                        activity.toolbar.setTitle("Total Selected posts = " + (count));
                        if (hitLists != null) {
                            hitLists.clear();
                            hitLists = new ArrayList<>();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    api_in_progress = 0;
                    recyclerView.setVisibility(View.VISIBLE);
                    hitLists.addAll(dataModel.getHits());
                    bindList(hitLists);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void bindList(List<DataModel.HitList> hitLists) {
        //activity.toolbar.setTitle("Total displaying posts = " + hitLists.size());
        tagsRecyclerViewAdapter = new TagsRecyclerViewAdapter(activity, hitLists);
        tagsRecyclerViewAdapter.delegate = this;
        recyclerView.setAdapter(tagsRecyclerViewAdapter);
        tagsRecyclerViewAdapter.setLoadMoreListener(new TagsRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hitLists.size() > 0) {
                            progressBar.setVisibility(View.GONE);
                            loadMoreHitsData();
                        }
                    }
                });
            }
        });
    }

    private void loadMoreHitsData() {
        api_in_progress = 1;
        pagenumber++;
        progressBar.setVisibility(View.VISIBLE);
        dataViewModel.getTagsData();

        tagsRecyclerViewAdapter.setLoadMoreListener(new TagsRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hitLists.size() > 0) {
                            progressBar.setVisibility(View.GONE);
                            loadMoreHitsData();
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

    @Override
    public void onRefresh() {
        //TODO make api call to get refreshed list of pagenumber 1 -- also clear the previous all data
        swipeRefreshLayout.setRefreshing(true);
        pagenumber = 1;
        dataViewModel.getTagsData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


    @Override
    public void OnPostClicked(DataModel.HitList hitList) {
        if (hitList != null) {
            if (hitList.isSelected()) {
                count = count + 1;
                activity.toolbar.setTitle("Total Selected posts = " + (count));
            } else {
                count = count - 1;
                activity.toolbar.setTitle("Total Selected posts = " + (count));
            }
        }
    }
}
