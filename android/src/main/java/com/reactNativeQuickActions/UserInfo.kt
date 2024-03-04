package com.reactNativeQuickActions

import android.os.Parcelable
import android.os.PersistableBundle
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val url: String? = null
) : Parcelable {

    fun toWritableMap(): WritableMap {
        val map = Arguments.createMap().apply {
            putString("url", url)
        }

        return map
    }

    fun toPersistableBundle(): PersistableBundle {
        return PersistableBundle().apply {
            putString("url", url)
        }
    }

    companion object {
        fun fromReadableMap(map: ReadableMap?): UserInfo? {

            if (map == null) {
                return null
            }

            return UserInfo(
                url = map.getString("url")
            )
        }

    }
}
