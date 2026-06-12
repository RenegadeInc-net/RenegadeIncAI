package me.devnull.renegadeincai.ui.context

import androidx.compose.runtime.staticCompositionLocalOf
import me.devnull.renegadeincai.data.datastore.Settings

val LocalSettings = staticCompositionLocalOf<Settings> {
    error("No SettingsStore provided")
}
