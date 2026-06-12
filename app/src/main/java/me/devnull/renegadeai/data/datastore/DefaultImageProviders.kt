package me.devnull.renegadeai.data.datastore

import me.rerere.ai.provider.ImageProviderSetting
import me.rerere.ai.provider.Model
import me.rerere.ai.provider.Modality
import me.rerere.ai.provider.ModelAbility
import kotlin.uuid.Uuid

val DEFAULT_IMAGE_PROVIDER_AUTO_MODEL_ID = Uuid.parse("e1111111-1111-1111-1111-111111111111")

val DEFAULT_IMAGE_PROVIDERS = listOf(
    ImageProviderSetting.OpenAI(
        id = Uuid.parse("11111111-1111-4111-8111-111111111111"),
        name = "OpenAI Images",
        apiKey = "",
        baseUrl = "https://api.openai.com/v1",
        builtIn = true,
        models = listOf(
            Model(
                id = DEFAULT_IMAGE_PROVIDER_AUTO_MODEL_ID,
                modelId = "gpt-image-1",
                displayName = "OpenAI Image",
                type = me.rerere.ai.provider.ModelType.IMAGE,
                inputModalities = listOf(Modality.TEXT),
                outputModalities = listOf(Modality.IMAGE),
                abilities = listOf(ModelAbility.TOOL)
            )
        )
    ),
    ImageProviderSetting.Automatic1111(
        id = Uuid.parse("22222222-2222-4222-8222-222222222222"),
        name = "Local AUTOMATIC1111",
        baseUrl = "http://127.0.0.1:7860",
        apiPath = "/sdapi/v1/txt2img",
        builtIn = true
    ),
    ImageProviderSetting.ComfyUI(
        id = Uuid.parse("33333333-3333-4333-8333-333333333333"),
        name = "ComfyUI",
        baseUrl = "http://127.0.0.1:8188",
        builtIn = true
    )
)
