package com.tiizzer.narz.pandasoft.challenge.repository

import com.tiizzer.narz.pandasoft.challenge.repository.model.request.LikeRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.request.LoginRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.request.RefreshTokenRequest
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.AuthenticationResponse
import com.tiizzer.narz.pandasoft.challenge.repository.model.response.HomeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppApiRepository {
    @POST("api/v1/login")
    fun authenticate(@Body data: LoginRequest): Call<AuthenticationResponse>

    @POST("api/v1/refresh")
    fun refreshToken(@Body refreshToken: RefreshTokenRequest): Call<AuthenticationResponse>

    @GET("api/v1/news")
    fun getHomeListData(): Call<HomeResponse>

    @POST("api/v1/like")
    fun likeNews(@Body like: LikeRequest): Call<String>
}