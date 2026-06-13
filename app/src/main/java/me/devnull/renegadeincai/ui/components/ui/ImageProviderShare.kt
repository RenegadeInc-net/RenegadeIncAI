package me.devnull.renegadeincai.ui.components.ui

import me.devnull.renegadeincai.ai.provider.ImageProviderSetting
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.util.Base64

fun encodeImageProviderSetting(setting: ImageProviderSetting): String {
    val json = Json.encodeToString(setting)
    return Base64.encodeToString(json.toByteArray(), Base64.NO_WRAP or Base64.URL_SAFE)
}

fun decodeImageProviderSetting(encoded: String): ImageProviderSetting {
    val json = String(Base64.decode(encoded, Base64.NO_WRAP or Base64.URL_SAFE))
    return Json.decodeFromString(json)
}
