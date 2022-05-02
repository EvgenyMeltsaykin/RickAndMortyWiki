package com.wiki.i_episode

import com.wiki.cf_network.HttpClient
import com.wiki.i_episode.data.EpisodeInfoResponse
import com.wiki.i_episode.data.EpisodesResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeApiService {

    @GET(EPISODE)
    suspend fun getAllEpisodes(
        @Query("page") page: Int
    ): EpisodesResponse

    @GET("$EPISODE/{id}")
    suspend fun getEpisode(
        @Path("id") id: String
    ): EpisodeInfoResponse

    @GET("$EPISODE/{ids}")
    suspend fun getEpisodes(
        @Path("ids") ids: String
    ): List<EpisodeInfoResponse>

    @GET(EPISODE)
    suspend fun getEpisodesByName(
        @Query("page") page: Int,
        @Query("name") name: String
    ): EpisodesResponse

    companion object {
        private const val EPISODE = "episode"
        fun create(
            okHttpClient: OkHttpClient
        ): EpisodeApiService {
            return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpClient.BASE_URL)
                .build()
                .create(EpisodeApiService::class.java)
        }
    }
}