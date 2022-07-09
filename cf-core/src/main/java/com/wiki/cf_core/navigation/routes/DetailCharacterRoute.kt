package com.wiki.cf_core.navigation.routes

import com.wiki.cf_core.navigation.base.route.FragmentRoute
import com.wiki.cf_core.navigation.base.route.RouteConfig
import com.wiki.cf_data.CharacterDto

class DetailCharacterRoute(
    val character: CharacterDto
) : FragmentRoute() {

    override var routeConfig: RouteConfig = RouteConfig()

}