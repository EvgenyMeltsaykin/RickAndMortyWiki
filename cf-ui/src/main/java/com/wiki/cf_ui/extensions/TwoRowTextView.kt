package com.wiki.cf_ui.extensions

import androidx.core.view.isVisible
import com.wiki.cf_ui.custom_view.TwoRowTextView

fun TwoRowTextView.setTextOrGone(firstRowText: String = this.firstRowText, secondRowText: String = this.secondRowText) {
    this.firstRowText = firstRowText
    this.secondRowText = secondRowText
    this.isVisible = firstRowText.isNotEmpty() && secondRowText.isNotEmpty()
}