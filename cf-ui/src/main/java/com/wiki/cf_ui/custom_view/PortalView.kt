package com.wiki.cf_ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.wiki.cf_ui.databinding.ViewLoaderBinding

class PortalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewLoaderBinding = ViewLoaderBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
        Glide.with(context)
            .load(com.wiki.cf_ui.R.drawable.portal_animation)
            .into(binding.root)
    }

}