package me.devnull.renegadeincai.tts.provider

import android.content.Context
import kotlinx.coroutines.flow.Flow
import me.devnull.renegadeincai.tts.model.AudioChunk
import me.devnull.renegadeincai.tts.model.TTSRequest

interface TTSProvider<T : TTSProviderSetting> {
    fun generateSpeech(
        context: Context,
        providerSetting: T,
        request: TTSRequest
    ): Flow<AudioChunk>
}
