package com.reactNativeQuickActions

import com.facebook.react.bridge.ReadableMap

fun ReadableMap.requireString(key: String): String {
    return getString(key) ?: throw IllegalArgumentException("No value found in ReadableMap for key: $key.")
}