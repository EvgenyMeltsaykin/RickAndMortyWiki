package com.wiki.cf_data.common

import java.io.Serializable

data class SimpleData(
    val value: String,
    val id: String
) : Serializable