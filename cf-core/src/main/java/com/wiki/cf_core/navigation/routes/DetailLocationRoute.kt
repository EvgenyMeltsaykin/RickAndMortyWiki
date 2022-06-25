package com.wiki.cf_core.navigation.routes

import com.wiki.cf_core.navigation.base.BaseRoute
import com.wiki.cf_core.navigation.base.RouteConfig
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.LocationDto
import com.wiki.cf_data.common.SimpleData

class DetailLocationRoute(
    val location: LocationDto?,
    val locationData: SimpleData?
): BaseRoute() {

    override var routeConfig: RouteConfig = RouteConfig()

}