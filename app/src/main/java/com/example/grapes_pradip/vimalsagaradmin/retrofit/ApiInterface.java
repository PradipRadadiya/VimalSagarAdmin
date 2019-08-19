package com.example.grapes_pradip.vimalsagaradmin.retrofit;

import com.example.grapes_pradip.vimalsagaradmin.bean.AllInformationModel;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.CompetitionWinner;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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

    @FormUrlEncoded
    @POST("bypeople/editpost")
    Call<JsonObject> editPost(@Field("Title") String title,
                              @Field("Post") String post,
                              @Field("pid") String pid,
                              @Field("VideoLink") String video_link);

    @FormUrlEncoded
    @POST("comment/editcomment")
    Call<JsonObject> editComment(@Field("cid") String cid,
                                 @Field("module") String module,
                                 @Field("comment") String comment);


    @FormUrlEncoded
    @POST("admin/login")
    Call<JsonObject> login(@Field("username") String username,
                           @Field("password") String password,
                           @Field("DeviceToken") String DeviceToken);

//    Favourite for all module

    @FormUrlEncoded
    @POST("audio/addfavorite")
    Call<JsonObject> addFavoriteAudio(@Field("aid") String aid,
                                      @Field("uid") String uid);


    @FormUrlEncoded
    @POST("audio/unfavorite")
    Call<JsonObject> addUnFavoriteAudio(@Field("aid") String aid,
                                        @Field("uid") String uid);


    @FormUrlEncoded
    @POST("competition/getcompetitionwinner")
    Call<CompetitionWinner> getCompetitionWinner(@Field("from_date") String from_date,
                                                 @Field("to_date") String to_date,
                                                 @Field("attended_competition") String attended_competition,
                                                 @Field("percentage") String percentage);


    @FormUrlEncoded
    @POST("competition/updatecompetitionresultnote")
    Call<JsonObject> saveCompetitionResultNote(@Field("Title") String title,
                                               @Field("Description") String description,
                                               @Field("Start_Time") String start_time,
                                               @Field("End_Time") String end_time,
                                               @Field("attend_comp") String attended_competition,
                                               @Field("flag") String flag);


    @GET("competition/getcompetitionresultnote")
    Call<JsonObject> getCompetitionResultNote();



    @Headers({ "X-ECM-API-ID: dac15f77-e3b7-4871-aa55-b3bbf2dfd863",
            "X-ECM-API-KEY: 12fa0ede83365a179ab2dadedc9ad22b3d837ca8",
            "X-CP-API-ID: b7da06a4",
            "X-CP-API-KEY: dea2322dfbd920446d1154a380f42924"})
    @GET("routers/")
    Call<JsonObject> getResponseData();


}
