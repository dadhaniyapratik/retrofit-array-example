package com.example.pratik.retrofit;

import com.example.pratik.retrofit.Pojo.UserDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Pratik on 29-Nov-16.
 */

public interface RetrofitArrayAPI {
    @GET("/users")
    Call<List<UserDetail>> getStudentDetails();
}
