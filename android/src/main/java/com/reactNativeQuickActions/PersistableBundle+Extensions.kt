package com.reactNativeQuickActions

import android.os.PersistableBundle

fun PersistableBundle.requireString(key: String): String {
    return getString(key) ?: throw IllegalArgumentException("No value found in PersistableBundle for key: $key.")
}