package me.devnull.renegadeincai.data.event

sealed class AppEvent {
    data class Speak(val text: String) : AppEvent()
}
