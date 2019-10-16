package com.example.engineerdemo.webcalls;



import com.example.engineerdemo.model.DataModel;

import io.reactivex.Observable;
import retrofit2.http.Query;

public abstract class MyApiService {

    private static MyApiService service;

    private static void setServiceType() {
        service = new RemoteDataService();
    }

    public static MyApiService getService() {
        if (service == null) {
            setServiceType();
        }

        return service;
    }

    // getting Data
    public abstract Observable<DataModel> getTagsData(@Query("page") int page);


}