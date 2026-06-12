package me.devnull.renegadeai.ui.components.ui

import me.rerere.ai.provider.ImageProviderSetting
import me.devnull.renegadeai.utils.JsonInstant
import kotlin.io.encoding.Base64

fun ImageProviderSetting.encodeForShare(): String {
    return buildString {
        append("ai-image-provider:")
        append("v1:")

        val value = JsonInstant.encodeToString(this@encodeForShare)
        append(Base64.encode(value.encodeToByteArray()))
    }
}

fun decodeImageProviderSetting(value: String): ImageProviderSetting {
    require(value.startsWith("ai-image-provider:v1:")) { "Invalid image provider setting string" }

    val base64Str = value.removePrefix("ai-image-provider:v1:")
    val jsonBytes = Base64.decode(base64Str)
    val jsonStr = jsonBytes.decodeToString()

    return JsonInstant.decodeFromString<ImageProviderSetting>(jsonStr)
}
