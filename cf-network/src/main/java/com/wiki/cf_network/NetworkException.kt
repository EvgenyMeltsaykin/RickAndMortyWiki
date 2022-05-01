package com.wiki.cf_network

import java.io.IOException

sealed class NetworkException(val messageError: String) : IOException() {

    object NoConnectivity : NetworkException("No internet connection")
    object Other : NetworkException("Unexpected error")

}