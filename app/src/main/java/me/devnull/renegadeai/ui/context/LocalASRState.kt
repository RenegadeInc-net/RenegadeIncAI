package me.devnull.renegadeai.ui.context

import androidx.compose.runtime.compositionLocalOf
import me.devnull.renegadeai.ui.hooks.CustomAsrState

val LocalASRState = compositionLocalOf<CustomAsrState> { error("Not provided yet") }

