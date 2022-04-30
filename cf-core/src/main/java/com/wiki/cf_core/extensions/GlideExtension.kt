package com.wiki.cf_core.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

fun RequestOptions.roundCorners(
    radius: Int,
    cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL
): RequestOptions {
    return bitmapTransform(RoundedCornersTransformation(radius, 0, cornerType))
}

fun RequestBuilder<Bitmap>.requestLoadBitmap(
    onResourceReady: (resource: Bitmap, transition: Transition<in Bitmap?>?) -> Unit,
    onLoadCleared: (placeholder: Drawable?) -> Unit = { }
): CustomTarget<Bitmap?> {
    return into(object : CustomTarget<Bitmap?>() {
        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap?>?
        ) {
            onResourceReady(resource, transition)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            onLoadCleared(placeholder)
        }
    })
}

fun RequestBuilder<Drawable>.requestListener(
    onLoadFailed: (GlideException?, Any?, Target<Drawable>?, Boolean) -> Unit,
    onResourceReady: (Drawable?, Any?, Target<Drawable>?, DataSource?, Boolean) -> Unit
): RequestBuilder<Drawable> {
    return listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean,
        ): Boolean {
            onLoadFailed(e, model, target, isFirstResource)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean,
        ): Boolean {
            onResourceReady(resource, model, target, dataSource, isFirstResource)
            return false
        }
    })
}