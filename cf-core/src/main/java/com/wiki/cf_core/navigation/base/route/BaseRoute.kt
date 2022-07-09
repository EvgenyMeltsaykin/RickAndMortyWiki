package com.wiki.cf_core.navigation.base.route

import java.io.Serializable

abstract class BaseRoute<T> : Serializable, RouteSetting {

    open fun getId(): String? = this::class.simpleName

}