package me.devnull.renegadeai.ui.context

import androidx.compose.runtime.compositionLocalOf
import me.devnull.renegadeai.ui.hooks.CustomTtsState

val LocalTTSState = compositionLocalOf<CustomTtsState> { error("Not provided yet") }
