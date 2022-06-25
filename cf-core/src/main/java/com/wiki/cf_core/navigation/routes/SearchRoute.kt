package com.wiki.cf_core.navigation.routes

import com.wiki.cf_core.navigation.base.BaseRoute
import com.wiki.cf_core.navigation.base.RouteConfig
import com.wiki.cf_data.CharacterDto
import com.wiki.cf_data.SearchFeature

class SearchRoute(
    val feature: SearchFeature
): BaseRoute() {

    override var routeConfig: RouteConfig = RouteConfig()

}