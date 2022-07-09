package com.wiki.cf_core.navigation.routes

import com.wiki.cf_core.navigation.base.route.FragmentRoute
import com.wiki.cf_core.navigation.base.route.RouteConfig
import com.wiki.cf_data.SearchFeature

class SearchRoute(
    val feature: SearchFeature
) : FragmentRoute() {

    override var routeConfig: RouteConfig = RouteConfig(
        isVisibleBottomNavigation = false
    )

}