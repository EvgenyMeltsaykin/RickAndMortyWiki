package com.wiki.cf_network.interceptors

import com.wiki.cf_network.NetworkException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException

class HttpErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            val response = chain.proceed(request)
            if (response.code == 404) {
                throw NetworkException.NothingFound
            }
            if (response.code != 200 && response.code != 201) {
                throw NetworkException.ServerError
            }
            response
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    throw NetworkException.ServerError
                }
                else -> throw e
            }
        }
    }

}