package com.reactNativeQuickActions

import android.os.PersistableBundle
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap

data class UserInfo(
    val url: String? = null
) {
    fun toPersistableBundle(): PersistableBundle {
        val bundle = PersistableBundle()
        bundle.putString("url", url)
        return bundle
    }

    fun toWritableMap(): WritableMap {
        val map = Arguments.createMap()
        map.putString("url", url)
        return map
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

        fun fromPersistableBundle(bundle: PersistableBundle?): UserInfo? {

            if (bundle == null) {
                return null
            }

            return UserInfo(
                url = bundle.getString("url")
            )
        }
    }
}
