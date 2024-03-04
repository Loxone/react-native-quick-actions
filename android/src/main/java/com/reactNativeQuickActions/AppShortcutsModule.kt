package com.reactNativeQuickActions

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.ShortcutManager
import android.net.Uri
import android.os.Build
import androidx.core.content.IntentCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.JSApplicationIllegalArgumentException
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.reactNativeQuickActions.ShortcutItem.Companion.fromReadableArray

@ReactModule(name = AppShortcutsModule.REACT_NAME)
internal class AppShortcutsModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    init {
        reactContext.addActivityEventListener(object : ActivityEventListener {
            override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
                // Do nothing
            }

            override fun onNewIntent(intent: Intent) {
                sendJSEvent(intent)
            }
        })
    }

    override fun getName(): String {
        return REACT_NAME
    }

    @ReactMethod
    @TargetApi(25)
    fun popInitialAction(promise: Promise) {
        try {
            val currentActivity = currentActivity
            var map: WritableMap? = null
            if (currentActivity != null) {
                val intent = currentActivity.intent
                if (ACTION_SHORTCUT == intent.action) {
                    val shortcutItem: ShortcutItem? = IntentCompat.getParcelableExtra(intent, SHORTCUT_ITEM, ShortcutItem::class.java)
                    if (shortcutItem != null) {
                        map = shortcutItem.toWritableMap()
                    }
                }
            }
            promise.resolve(map)
        } catch (e: Exception) {
            promise.reject(
                JSApplicationIllegalArgumentException(
                    "AppShortcuts.popInitialAction error. " + e.message
                )
            )
        }
    }

    @ReactMethod
    @TargetApi(25)
    fun setShortcutItems(items: ReadableArray) {
        if (!isShortcutSupported || items.size() == 0) {
            return
        }
        val currentActivity = currentActivity ?: return

        val shortcutItems = fromReadableArray(items)
        val shortcuts = shortcutItems.mapIndexed { index: Int, item: ShortcutItem ->
            return@mapIndexed createShortcutInfo(currentActivity, id = "id$index", item)
        }

        ShortcutManagerCompat.setDynamicShortcuts(reactApplicationContext, shortcuts)
    }

    private fun createShortcutInfo(activity: Activity, id: String, item: ShortcutItem): ShortcutInfoCompat {

        val context = reactApplicationContext
        val iconResId = context.resources.getIdentifier(item.icon, "drawable", context.packageName)
        val uri: Uri? = item.userInfo?.url?.toUri()

        val intent: Intent = if (uri != null) {
            Intent(Intent.ACTION_VIEW, uri)
        } else {
            Intent(context, activity.javaClass)
                .setAction(ACTION_SHORTCUT)
        }

        intent.putExtra(SHORTCUT_ITEM, item)

        return ShortcutInfoCompat.Builder(context, id)
            .setShortLabel(item.title)
            .setLongLabel(item.title)
            .setIcon(IconCompat.createWithResource(context, iconResId))
            .setIntent(intent)
            .build()
    }

    @ReactMethod
    @TargetApi(25)
    fun clearShortcutItems() {
        if (!isShortcutSupported) {
            return
        }
        reactApplicationContext.getSystemService(ShortcutManager::class.java).removeAllDynamicShortcuts()
    }

    @ReactMethod
    fun isSupported(callback: Callback?) {
        callback?.invoke(isShortcutSupported)
    }

    private val isShortcutSupported: Boolean
        get() = Build.VERSION.SDK_INT >= 25

    private fun sendJSEvent(intent: Intent) {
        if (ACTION_SHORTCUT != intent.action || !isShortcutSupported) {
            return
        }
        val item: ShortcutItem? = IntentCompat.getParcelableExtra(intent, SHORTCUT_ITEM, ShortcutItem::class.java)
        if (item != null) {
            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("quickActionShortcut", item.toWritableMap())
        }
    }

    companion object {
        const val REACT_NAME = "ReactAppShortcuts"
        private const val ACTION_SHORTCUT = "ACTION_SHORTCUT"
        private const val SHORTCUT_ITEM = "SHORTCUT_ITEM"
    }
}
