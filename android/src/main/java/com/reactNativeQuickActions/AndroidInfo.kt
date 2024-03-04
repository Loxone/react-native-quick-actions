package com.reactNativeQuickActions

import android.os.Parcelable
import android.os.PersistableBundle
import com.facebook.react.bridge.ReadableMap
import kotlinx.parcelize.Parcelize

@Parcelize
data class AndroidInfo(
    /**
     * Fully qualified class name that will be used when setting up the shortcut-intent.
     */
    val className: String?
) : Parcelable {
    fun toPersistableBundle(): PersistableBundle {
        return PersistableBundle().apply {
            putString("className", className)
        }
    }

    companion object {
        fun fromReadableMap(map: ReadableMap?): AndroidInfo? {

            if (map == null) {
                return null
            }

            return AndroidInfo(
                map.getString("className")
            )
        }
    }
}
