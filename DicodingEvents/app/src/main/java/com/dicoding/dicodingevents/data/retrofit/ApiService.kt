package com.dicoding.dicodingevents.data.retrofit

import com.dicoding.dicodingevents.data.response.DetailEventResponse
import com.dicoding.dicodingevents.data.response.ListEventsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getListEvents(
        @Query("active") active : Int,
        @Query("q") query : String? = null,
        @Query("limit") limit : Int = 40
    ) : Call<ListEventsResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id : Int
    ) : Call<DetailEventResponse>
}