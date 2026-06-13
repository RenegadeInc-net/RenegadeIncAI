package me.devnull.renegadeincai.ui.context

import androidx.compose.runtime.compositionLocalOf
import me.devnull.renegadeincai.ui.hooks.CustomAsrState

val LocalASRState = compositionLocalOf<CustomAsrState> { error("Not provided yet") }

