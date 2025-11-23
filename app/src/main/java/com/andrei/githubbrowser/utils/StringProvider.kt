package com.andrei.githubbrowser.utils

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes stringResId: Int): String
}