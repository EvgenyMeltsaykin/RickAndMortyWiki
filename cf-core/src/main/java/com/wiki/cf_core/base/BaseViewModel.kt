package com.wiki.cf_core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiki.cf_network.NetworkException
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    fun launchInternetRequest(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: NetworkException) {
                //showSnackBar(e.messageError)
            }
        }
    }
}