package com.example.engineerdemo.webcalls;

import com.example.engineerdemo.model.DataModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RemoteDataService extends MyApiService {

    //Holds service instance
    private MyService service;

    // Constructor
    RemoteDataService() {
        service = new RetrofitHelper().getMyService();
    }


    @Override
    public Observable<DataModel> getTagsData(int page) {
        return service.getTagsDta(page)
                .subscribeOn(Schedulers.io()) // “work” on io thread
                .observeOn(AndroidSchedulers.mainThread()); // “listen” on UIThread;
    }
}
