package com.wiki.cf_core.navigation.base.route

import com.wiki.cf_core.navigation.animation_transitions.TransitionType
import java.io.Serializable

interface RouteSetting {
    var routeConfig: RouteConfig
}

data class RouteConfig(
    val animation: TransitionType = TransitionType.SIMPLE,
    val isVisibleBottomNavigation: Boolean = true
) : Serializable