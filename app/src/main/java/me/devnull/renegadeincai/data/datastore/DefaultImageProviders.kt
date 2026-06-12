package me.devnull.renegadeincai.data.datastore

import me.devnull.renegadeincai.ai.provider.ImageProviderSetting
import me.devnull.renegadeincai.ai.provider.Model
import me.devnull.renegadeincai.ai.provider.Modality
import me.devnull.renegadeincai.ai.provider.ModelAbility
import me.devnull.renegadeincai.ai.provider.ModelType
import kotlin.uuid.Uuid

val DEFAULT_IMAGE_PROVIDERS = listOf(
    ImageProviderSetting.OpenAI(
        id = Uuid.parse("79f7d6e6-993d-4c33-8a02-5c3b5d2e3f4a"),
        name = "OpenAI DALL-E",
        baseUrl = "https://api.openai.com/v1",
        imagePath = "/images/generations",
        apiKey = "",
        enabled = true,
        models = listOf(
            Model(
                id = Uuid.parse("a1b2c3d4-e5f6-4a5b-b6c7-d8e9f0a1b2c3"),
                modelId = "dall-e-3",
                displayName = "DALL-E 3",
                type = ModelType.IMAGE,
                inputModalities = listOf(Modality.TEXT),
                outputModalities = listOf(Modality.IMAGE),
                abilities = emptyList()
            )
        )
    ),
    ImageProviderSetting.Automatic1111(
        id = Uuid.parse("b1c2d3e4-f5a6-4b7c-c8d9-e0f1a2b3c4d5"),
        name = "Stable Diffusion",
        baseUrl = "http://127.0.0.1:7860",
        apiPath = "/sdapi/v1/txt2img",
        enabled = false
    ),
    ImageProviderSetting.ComfyUI(
        id = Uuid.parse("c1d2e3f4-a5b6-4c7d-d8e9-f0a1b2c3d4e5"),
        name = "ComfyUI",
        baseUrl = "http://127.0.0.1:8188",
        enabled = false
    )
)
