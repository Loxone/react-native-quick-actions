package com.reactNativeQuickActions

import android.os.Parcelable
import android.os.PersistableBundle
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShortcutItem(
    val type: String? = null,
    val title: String,
    val icon: String? = null,
    val userInfo: UserInfo? = null,
    val androidInfo: AndroidInfo? = null
) : Parcelable {

    fun toWritableMap(): WritableMap {

        val map = Arguments.createMap().apply {
            putString("type", type)
            putString("title", title)
            putString("icon", icon)
            putMap("userInfo", userInfo?.toWritableMap())
        }

        return map
    }

    fun toPersistableBundle(): PersistableBundle {
        return PersistableBundle().apply {
            putString("type", type)
            putString("title", title)
            putString("icon", icon)
            putPersistableBundle("userInfo", userInfo?.toPersistableBundle())
        }
    }

    companion object {

        @JvmStatic
        fun fromReadableArray(items: ReadableArray): List<ShortcutItem> {
            val shortcuts = mutableListOf<ShortcutItem>()

            for (index in 0 until items.size()) {
                items.getMap(index)?.let { map ->
                    val shortcut: ShortcutItem = fromReadableMap(map)
                    shortcuts.add(shortcut)
                }

            }

            return shortcuts
        }

        @JvmStatic
        fun fromReadableMap(map: ReadableMap): ShortcutItem {
            val userInfo = map.getMap("userInfo")
            val androidInfo = map.getMap("androidInfo")

            return ShortcutItem(
                type = map.getString("type"),
                title = map.requireString("title"),
                icon = map.getString("icon"),
                userInfo = UserInfo.fromReadableMap(userInfo),
                androidInfo = AndroidInfo.fromReadableMap(androidInfo)
            )
        }

    }
}
