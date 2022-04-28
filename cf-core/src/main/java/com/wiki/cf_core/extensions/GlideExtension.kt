package com.wiki.cf_core.extensions

import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

fun RequestOptions.roundCorners(
    radius: Int,
    cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL
): RequestOptions {
    return bitmapTransform(RoundedCornersTransformation(radius, 0, cornerType))
}