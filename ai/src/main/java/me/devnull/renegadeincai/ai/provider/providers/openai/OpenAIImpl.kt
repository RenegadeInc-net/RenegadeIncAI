package me.devnull.renegadeincai.ai.provider.providers.openai

import kotlinx.coroutines.flow.Flow
import me.devnull.renegadeincai.ai.provider.ProviderSetting
import me.devnull.renegadeincai.ai.provider.TextGenerationParams
import me.devnull.renegadeincai.ai.ui.MessageChunk
import me.devnull.renegadeincai.ai.ui.UIMessage

interface OpenAIImpl {
    suspend fun generateText(
        providerSetting: ProviderSetting.OpenAI,
        messages: List<UIMessage>,
        params: TextGenerationParams,
    ): MessageChunk

    suspend fun streamText(
        providerSetting: ProviderSetting.OpenAI,
        messages: List<UIMessage>,
        params: TextGenerationParams,
    ): Flow<MessageChunk>
}
