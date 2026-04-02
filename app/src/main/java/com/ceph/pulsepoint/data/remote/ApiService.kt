package com.ceph.pulsepoint.data.remote

import com.ceph.pulsepoint.domain.model.NewsResponse
import com.ceph.pulsepoint.utils.Constants
import com.ceph.pulsepoint.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getSearchedNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getNewsByCategory(
        @Query("q") category: String,
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getTodayNews(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = API_KEY,
    ): NewsResponse

}