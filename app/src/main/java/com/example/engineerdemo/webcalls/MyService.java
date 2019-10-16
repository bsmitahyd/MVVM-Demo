package com.example.engineerdemo.webcalls;


import com.example.engineerdemo.ApiConstants;
import com.example.engineerdemo.model.DataModel;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyService {

    // TODO getting tags by date
    @GET(ApiConstants.GET_TAGS_URL)
    Observable<DataModel> getTagsDta(@Query("page") int page);
}