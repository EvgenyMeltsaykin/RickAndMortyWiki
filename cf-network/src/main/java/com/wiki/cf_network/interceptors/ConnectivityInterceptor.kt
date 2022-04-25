package com.wiki.cf_network.interceptors

import com.wiki.cf_network.NetworkException
import com.wiki.cf_network.util.ConnectivityService
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(
    private val connectivityService: ConnectivityService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (connectivityService.isOffline()) {
            throw NetworkException.NoConnectivity
        } else {
            return chain.proceed(chain.request())
        }
    }
}