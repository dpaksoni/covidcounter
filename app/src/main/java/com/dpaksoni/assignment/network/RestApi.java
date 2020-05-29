package com.dpaksoni.assignment.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {

    @GET("summary")
    Call<ResponseBody> getCounts();
}
