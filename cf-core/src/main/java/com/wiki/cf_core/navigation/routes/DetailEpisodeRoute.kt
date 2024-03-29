package com.wiki.cf_core.navigation.routes

import com.wiki.cf_core.navigation.base.route.FragmentRoute
import com.wiki.cf_core.navigation.base.route.RouteConfig
import com.wiki.cf_data.EpisodeDto

class DetailEpisodeRoute(
    val episode: EpisodeDto
) : FragmentRoute() {

    override var routeConfig: RouteConfig = RouteConfig()

}