package com.wiki.i_location

import com.wiki.cf_network.HttpClient
import com.wiki.i_location.data.LocationInfoResponse
import com.wiki.i_location.data.LocationsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApiService {

    @GET(LOCATION)
    suspend fun getAllLocations(
        @Query("page") page: Int
    ): LocationsResponse

    @GET("$LOCATION/{id}")
    suspend fun getLocation(
        @Path("id") id: String
    ): LocationInfoResponse

    @GET("$LOCATION/{ids}")
    suspend fun getLocations(
        @Path("ids") ids: String
    ): List<LocationInfoResponse>

    companion object {
        private const val LOCATION = "location"
        fun create(
            okHttpClient: OkHttpClient
        ): LocationApiService {
            return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpClient.BASE_URL)
                .build()
                .create(LocationApiService::class.java)
        }
    }
}