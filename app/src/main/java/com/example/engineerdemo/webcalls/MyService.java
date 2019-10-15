package com.example.engineerdemo.webcalls;


import com.example.engineerdemo.model.DataModel;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyService {

    // TODO getting tags by date
    @GET("search_by_date?tags=story")
    Observable<DataModel> getTagsDta(@Query("page") int page);
}