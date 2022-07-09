package com.wiki.cf_core.navigation.routes

import com.wiki.cf_core.navigation.base.route.FragmentRoute
import com.wiki.cf_core.navigation.base.route.RouteConfig
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData

class DetailLocationRoute(
    val location: LocationDto?,
    val locationData: SimpleData?
) : FragmentRoute() {

    override var routeConfig: RouteConfig = RouteConfig()

}