package me.devnull.renegadeincai.ui.context

import androidx.compose.runtime.compositionLocalOf
import me.devnull.renegadeincai.ui.hooks.CustomTtsState

val LocalTTSState = compositionLocalOf<CustomTtsState> { error("Not provided yet") }
