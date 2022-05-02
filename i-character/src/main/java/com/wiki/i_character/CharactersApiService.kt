package com.wiki.i_character

import com.wiki.cf_network.HttpClient
import com.wiki.i_character.data.CharacterInfoResponse
import com.wiki.i_character.data.CharactersResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApiService {

    @GET(CHARACTER)
    suspend fun getAllCharacters(
        @Query("page") page: Int
    ): CharactersResponse

    @GET("$CHARACTER/{id}")
    suspend fun getCharacter(
        @Path("id") id: String
    ): CharacterInfoResponse

    @GET("$CHARACTER/{ids}")
    suspend fun getCharacters(
        @Path("ids") ids: String
    ): List<CharacterInfoResponse>

    @GET(CHARACTER)
    suspend fun getCharactersByName(
        @Query("page") page: Int,
        @Query("name") name: String
    ): CharactersResponse

    companion object {
        private const val CHARACTER = "character"
        fun create(
            okHttpClient: OkHttpClient
        ): CharactersApiService {
            return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpClient.BASE_URL)
                .build()
                .create(CharactersApiService::class.java)
        }
    }
}