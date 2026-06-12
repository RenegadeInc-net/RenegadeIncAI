package me.devnull.renegadeai.ui.context

import androidx.compose.runtime.staticCompositionLocalOf
import me.devnull.renegadeai.data.datastore.Settings

val LocalSettings = staticCompositionLocalOf<Settings> {
    error("No SettingsStore provided")
}
