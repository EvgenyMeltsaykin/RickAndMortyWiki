package com.wiki.cf_core.navigation.commands

import com.github.terrakok.cicerone.Command
import com.wiki.cf_core.navigation.TabKey
import com.wiki.cf_core.navigation.base.route.DialogRoute

data class ChangeTab(val tabKey: TabKey) : Command

data class ShowDialog(val dialog: DialogRoute) : Command