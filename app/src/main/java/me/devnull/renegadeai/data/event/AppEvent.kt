package me.devnull.renegadeai.data.event

sealed class AppEvent {
    data class Speak(val text: String) : AppEvent()
}
