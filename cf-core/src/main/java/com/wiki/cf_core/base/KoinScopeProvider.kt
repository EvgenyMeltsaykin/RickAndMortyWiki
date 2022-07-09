package com.wiki.cf_core.base

import org.koin.android.scope.AndroidScopeComponent
import org.koin.core.scope.Scope

interface KoinScopeProvider : AndroidScopeComponent {
    override val scope: Scope
}