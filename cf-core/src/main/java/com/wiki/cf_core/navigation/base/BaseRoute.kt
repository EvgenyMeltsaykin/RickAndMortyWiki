package com.wiki.cf_core.navigation.base

import com.wiki.cf_core.navigation.animation_transitions.TransitionType
import java.io.Serializable

data class RouteConfig(
    val animation: TransitionType = TransitionType.SIMPLE,
)

abstract class BaseRoute : Serializable {

    abstract var routeConfig: RouteConfig

}