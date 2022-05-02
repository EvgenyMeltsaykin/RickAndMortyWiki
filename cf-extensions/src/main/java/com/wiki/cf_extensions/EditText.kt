package com.wiki.cf_extensions

import android.text.TextUtils
import android.widget.EditText

/**
 * Обновляет текст в поле ввода только если он изменился, сохраняя позицию курсора
 */
var EditText.distinctText: CharSequence
    get() = this.text.toString()
    set(value) {
        if (TextUtils.equals(value, this.text.toString())) return
        tag = false
        this.text?.let { it.replace(0, it.length, value) }
        tag = true
    }