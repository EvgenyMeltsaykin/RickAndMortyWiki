package com.wiki.cf_core.navigation.base

import com.github.terrakok.cicerone.*
import com.wiki.cf_core.navigation.ScreenProvider
import com.wiki.cf_core.navigation.base.route.BaseRoute
import com.wiki.cf_core.navigation.base.route.DialogRoute
import com.wiki.cf_core.navigation.base.route.FragmentRoute
import com.wiki.cf_core.navigation.commands.ShowDialog

open class BaseAppRouter(
    val screenProvider: ScreenProvider
) : BaseRouter() {

    fun navigateTo(route: BaseRoute<*>) {
        executeCommands(getForwardCommandByRoute(route))
    }

    fun newRootScreen(screen: Screen) {
        executeCommands(BackTo(null), Replace(screen))
    }

    fun backToRoot() {
        executeCommands(BackTo(null))
    }

    fun replace(screen: Screen) {
        executeCommands(Replace(screen))
    }

    fun replace(route: BaseRoute<*>) {
        executeCommands(getReplaceCommandByRoute(route))
    }

    fun backTo(route: BaseRoute<*>?) {
        executeCommands(getBackToCommandByRoute(route))
    }

    fun newChain(routes: List<BaseRoute<*>>) {
        newChain(*routes.toTypedArray())
    }

    fun newChain(vararg routes: BaseRoute<*>) {
        val commands = routes.map { getForwardCommandByRoute(it) }
        executeCommands(*commands.toTypedArray())
    }

    fun newRootChain(vararg routes: BaseRoute<*>) {
        val commands = routes.mapIndexed { index, route ->
            when {
                route is DialogRoute -> getForwardCommandByRoute(route)
                index == 0 -> getReplaceCommandByRoute(route)
                else -> getForwardCommandByRoute(route)
            }
        }
        executeCommands(BackTo(null), *commands.toTypedArray())
    }

    fun newRootChain(routes: List<BaseRoute<*>>) {
        newRootChain(*routes.toTypedArray())
    }

    fun finishChain() {
        executeCommands(BackTo(null), Back())
    }

    fun back() {
        executeCommands(Back())
    }

    open fun getReplaceCommandByRoute(route: BaseRoute<*>): Replace {
        return when (route) {
            is FragmentRoute -> Replace(screenProvider.byRoute(route))
            else -> error("Not supported replace command for route type $route")
        }
    }

    open fun getForwardCommandByRoute(route: BaseRoute<*>): Command {
        return when (route) {
            is FragmentRoute -> {
                val screen = screenProvider.byRoute(route)
                screen.clearContainer
                Forward(screenProvider.byRoute(route))
            }
            is DialogRoute -> ShowDialog(route)
            else -> error("Not supported forward command for route type $route")
        }
    }

    open fun getBackToCommandByRoute(route: BaseRoute<*>?): Command {
        return when (route) {
            is FragmentRoute -> BackTo(screenProvider.byRoute(route))
            null -> BackTo(null)
            else -> error("Not supported backTo command for route type $route")
        }
    }

}