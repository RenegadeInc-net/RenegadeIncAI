package me.devnull.renegadeincai.service

import kotlinx.serialization.json.JsonPrimitive
import me.devnull.renegadeincai.ai.core.ReasoningLevel
import me.devnull.renegadeincai.ai.provider.CustomBody
import me.devnull.renegadeincai.ai.provider.CustomHeader
import me.devnull.renegadeincai.ai.provider.Model
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatServiceTest {
    @Test
    fun `background generation params include model custom request configuration`() {
        val headers = listOf(CustomHeader(name = "X-Gateway-Token", value = "test-token"))
        val bodies = listOf(CustomBody(key = "gateway_mode", value = JsonPrimitive("strict")))
        val model = Model(
            modelId = "custom-chat-model",
            customHeaders = headers,
            customBodies = bodies,
        )

        val params = backgroundTextGenerationParams(model)

        assertEquals(model, params.model)
        assertEquals(ReasoningLevel.OFF, params.reasoningLevel)
        assertEquals(headers, params.customHeaders)
        assertEquals(bodies, params.customBody)
    }
}
