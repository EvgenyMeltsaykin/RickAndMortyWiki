package com.wiki.cf_ui.custom_view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.wiki.cf_ui.R
import com.wiki.cf_ui.databinding.ViewTwoRowTextBinding
import com.wiki.cf_ui.extensions.blurMask

class TwoRowTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewTwoRowTextBinding =
        ViewTwoRowTextBinding.inflate(LayoutInflater.from(context), this, false)

    var firstRowText: String = ""
        set(value) {
            binding.tvFirstRow.text = value
            field = value
            invalidate()
        }
    var secondRowText: String = ""
        set(value) {
            binding.tvSecondRow.text = value
            field = value
            invalidate()
        }
    var textSize: Float = 18f
    var textColor: Int = Color.BLACK
    var blurRadius: Float = 0f
        set(value) {
            binding.tvFirstRow.text = firstRowText.blurMask(value)
            binding.tvSecondRow.text = secondRowText.blurMask(value)
            field = value
            invalidate()
        }
    var gravity: Int = Gravity.START

    init {
        addView(binding.root)
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.TwoRowTextView,
            0, 0
        )

        firstRowText = typedArray.getString(R.styleable.TwoRowTextView_firstRowText).toString()
        secondRowText = typedArray.getString(R.styleable.TwoRowTextView_secondRowText).toString()
        textSize = typedArray.getDimensionPixelSize(R.styleable.TwoRowTextView_rowTextSize, 18).toFloat()
        textColor = typedArray.getColor(R.styleable.TwoRowTextView_rowTextColor, Color.BLACK)
        gravity = typedArray.getInt(R.styleable.TwoRowTextView_android_gravity, Gravity.START)
        typedArray.recycle()
        initView()

    }

    private fun initView() {
        with(binding) {
            tvFirstRow.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            tvSecondRow.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            tvFirstRow.setTextColor(textColor)
            tvSecondRow.setTextColor(textColor)
            tvFirstRow.gravity = gravity
            tvSecondRow.gravity = gravity
        }

    }
}