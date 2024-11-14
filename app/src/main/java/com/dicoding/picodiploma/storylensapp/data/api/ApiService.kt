package com.dicoding.picodiploma.storylensapp.data.api

import com.dicoding.picodiploma.storylensapp.data.response.AddResponse
import com.dicoding.picodiploma.storylensapp.data.response.DetailtStoryResponse
import com.dicoding.picodiploma.storylensapp.data.response.ListStoryResponse
import com.dicoding.picodiploma.storylensapp.data.response.LoginResponse
import com.dicoding.picodiploma.storylensapp.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String): RegisterResponse

    @GET("stories")
    suspend fun getListStory(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20): ListStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody, ): AddResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") storyId: String): DetailtStoryResponse

}