package com.wiki.cf_network

import java.io.IOException

sealed class NetworkException(val messageError: String) : IOException() {

    object NoConnectivity : NetworkException("Нет подключения к интернету")
    object Other : NetworkException("Непредвиденная ошибка")

}