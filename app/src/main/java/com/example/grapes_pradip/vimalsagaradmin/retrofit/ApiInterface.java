package com.example.grapes_pradip.vimalsagaradmin.retrofit;

import com.example.grapes_pradip.vimalsagaradmin.bean.AllInformationModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("admin/login/")
    Call<JsonObject> Login(@Field("username") String username,
                           @Field("password") String password,
                           @Field("password") String DeviceToken);


    @FormUrlEncoded
    @POST("info/getallinfo/")
    Call<AllInformationModel> getAllInfo(@Field("page") String page, @Field("psize") String pSize);


    @GET("user")
    Call<AllInformationModel> getUser(@Header("Authorization") String authorization);

}
