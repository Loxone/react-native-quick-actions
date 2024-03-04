package com.reactNativeQuickActions

import android.os.Parcelable
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
    val userInfo: UserInfo? = null
) : Parcelable {

    fun toWritableMap(): WritableMap {
        val map = Arguments.createMap()
        map.putString("type", type)
        map.putString("title", title)
        map.putString("icon", icon)
        map.putMap("userInfo", userInfo!!.toWritableMap())
        return map
    }

    companion object {

        @JvmStatic
        fun fromReadableArray(items: ReadableArray): List<ShortcutItem> {
            val shortcuts = mutableListOf<ShortcutItem>()

            for (index in 0 until items.size()) {
                val map: ReadableMap = items.getMap(index)
                val shortcut: ShortcutItem = fromReadableMap(map)

                shortcuts.add(shortcut)
            }

            return shortcuts
        }

        @JvmStatic
        fun fromReadableMap(map: ReadableMap): ShortcutItem {
            return ShortcutItem(
                type = map.getString("type"),
                title = map.requireString("title"),
                icon = map.getString("icon"),
                userInfo = UserInfo.fromReadableMap(map.getMap("userInfo"))
            )
        }

    }
}
