package me.devnull.renegadeincai.ai.provider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import androidx.compose.runtime.Composable
import kotlin.uuid.Uuid

@Serializable
sealed class ImageProviderSetting {
    abstract val id: Uuid
    abstract val enabled: Boolean
    abstract val name: String
    abstract val models: List<Model>

    abstract val builtIn: Boolean
    abstract val description: @Composable() () -> Unit
    abstract val shortDescription: @Composable() () -> Unit

    abstract fun addModel(model: Model): ImageProviderSetting
    abstract fun editModel(model: Model): ImageProviderSetting
    abstract fun delModel(model: Model): ImageProviderSetting
    abstract fun moveMove(from: Int, to: Int): ImageProviderSetting
    abstract fun copyProvider(
        id: Uuid = this.id,
        enabled: Boolean = this.enabled,
        name: String = this.name,
        models: List<Model> = this.models,
        builtIn: Boolean = this.builtIn,
        description: @Composable (() -> Unit) = this.description,
        shortDescription: @Composable (() -> Unit) = this.shortDescription,
    ): ImageProviderSetting

    @Serializable
    @SerialName("openai_image")
    data class OpenAI(
        override var id: Uuid = Uuid.random(),
        override var enabled: Boolean = true,
        override var name: String = "OpenAI Images",
        override var models: List<Model> = emptyList(),
        @Transient override val builtIn: Boolean = false,
        @Transient override val description: @Composable (() -> Unit) = {},
        @Transient override val shortDescription: @Composable (() -> Unit) = {},
        var apiKey: String = "",
        var baseUrl: String = "https://api.openai.com/v1",
        var imagePath: String = "/images/generations",
    ) : ImageProviderSetting() {
        override fun addModel(model: Model): ImageProviderSetting = copy(models = models + model)
        override fun editModel(model: Model): ImageProviderSetting = copy(models = models.map { if (it.id == model.id) model.copy() else it })
        override fun delModel(model: Model): ImageProviderSetting = copy(models = models.filter { it.id != model.id })
        override fun moveMove(from: Int, to: Int): ImageProviderSetting = copy(models = models.toMutableList().apply {
            val model = removeAt(from); add(to, model)
        })
        override fun copyProvider(id: Uuid, enabled: Boolean, name: String, models: List<Model>, builtIn: Boolean, description: @Composable (() -> Unit), shortDescription: @Composable (() -> Unit)): ImageProviderSetting {
            return this.copy(id = id, enabled = enabled, name = name, models = models, builtIn = builtIn, description = description, shortDescription = shortDescription)
        }
    }

    @Serializable
    @SerialName("google_image")
    data class Google(
        override var id: Uuid = Uuid.random(),
        override var enabled: Boolean = true,
        override var name: String = "Google Images",
        override var models: List<Model> = emptyList(),
        @Transient override val builtIn: Boolean = false,
        @Transient override val description: @Composable (() -> Unit) = {},
        @Transient override val shortDescription: @Composable (() -> Unit) = {},
        var apiKey: String = "",
        var baseUrl: String = "https://generativelanguage.googleapis.com/v1beta",
    ) : ImageProviderSetting() {
        override fun addModel(model: Model): ImageProviderSetting = copy(models = models + model)
        override fun editModel(model: Model): ImageProviderSetting = copy(models = models.map { if (it.id == model.id) model.copy() else it })
        override fun delModel(model: Model): ImageProviderSetting = copy(models = models.filter { it.id != model.id })
        override fun moveMove(from: Int, to: Int): ImageProviderSetting = copy(models = models.toMutableList().apply {
            val model = removeAt(from); add(to, model)
        })
        override fun copyProvider(id: Uuid, enabled: Boolean, name: String, models: List<Model>, builtIn: Boolean, description: @Composable (() -> Unit), shortDescription: @Composable (() -> Unit)): ImageProviderSetting {
            return this.copy(id = id, enabled = enabled, name = name, models = models, builtIn = builtIn, description = description, shortDescription = shortDescription)
        }
    }

    @Serializable
    @SerialName("claude_image")
    data class Claude(
        override var id: Uuid = Uuid.random(),
        override var enabled: Boolean = true,
        override var name: String = "Claude Images",
        override var models: List<Model> = emptyList(),
        @Transient override val builtIn: Boolean = false,
        @Transient override val description: @Composable (() -> Unit) = {},
        @Transient override val shortDescription: @Composable (() -> Unit) = {},
        var apiKey: String = "",
        var baseUrl: String = "https://api.anthropic.com/v1",
    ) : ImageProviderSetting() {
        override fun addModel(model: Model): ImageProviderSetting = copy(models = models + model)
        override fun editModel(model: Model): ImageProviderSetting = copy(models = models.map { if (it.id == model.id) model.copy() else it })
        override fun delModel(model: Model): ImageProviderSetting = copy(models = models.filter { it.id != model.id })
        override fun moveMove(from: Int, to: Int): ImageProviderSetting = copy(models = models.toMutableList().apply {
            val model = removeAt(from); add(to, model)
        })
        override fun copyProvider(id: Uuid, enabled: Boolean, name: String, models: List<Model>, builtIn: Boolean, description: @Composable (() -> Unit), shortDescription: @Composable (() -> Unit)): ImageProviderSetting {
            return this.copy(id = id, enabled = enabled, name = name, models = models, builtIn = builtIn, description = description, shortDescription = shortDescription)
        }
    }

    @Serializable
    @SerialName("automatic1111")
    data class Automatic1111(
        override var id: Uuid = Uuid.random(),
        override var enabled: Boolean = true,
        override var name: String = "StableDiffusion (AUTOMATIC1111)",
        override var models: List<Model> = emptyList(),
        @Transient override val builtIn: Boolean = false,
        @Transient override val description: @Composable (() -> Unit) = {},
        @Transient override val shortDescription: @Composable (() -> Unit) = {},
        var baseUrl: String = "http://127.0.0.1:7860",
        var apiPath: String = "/sdapi/v1/txt2img",
    ) : ImageProviderSetting() {
        override fun addModel(model: Model): ImageProviderSetting = copy(models = models + model)
        override fun editModel(model: Model): ImageProviderSetting = copy(models = models.map { if (it.id == model.id) model.copy() else it })
        override fun delModel(model: Model): ImageProviderSetting = copy(models = models.filter { it.id != model.id })
        override fun moveMove(from: Int, to: Int): ImageProviderSetting = copy(models = models.toMutableList().apply {
            val model = removeAt(from); add(to, model)
        })
        override fun copyProvider(id: Uuid, enabled: Boolean, name: String, models: List<Model>, builtIn: Boolean, description: @Composable (() -> Unit), shortDescription: @Composable (() -> Unit)): ImageProviderSetting {
            return this.copy(id = id, enabled = enabled, name = name, models = models, builtIn = builtIn, description = description, shortDescription = shortDescription)
        }
    }

    @Serializable
    @SerialName("comfyui")
    data class ComfyUI(
        override var id: Uuid = Uuid.random(),
        override var enabled: Boolean = true,
        override var name: String = "ComfyUI",
        override var models: List<Model> = emptyList(),
        @Transient override val builtIn: Boolean = false,
        @Transient override val description: @Composable (() -> Unit) = {},
        @Transient override val shortDescription: @Composable (() -> Unit) = {},
        var baseUrl: String = "http://127.0.0.1:8188",
    ) : ImageProviderSetting() {
        override fun addModel(model: Model): ImageProviderSetting = copy(models = models + model)
        override fun editModel(model: Model): ImageProviderSetting = copy(models = models.map { if (it.id == model.id) model.copy() else it })
        override fun delModel(model: Model): ImageProviderSetting = copy(models = models.filter { it.id != model.id })
        override fun moveMove(from: Int, to: Int): ImageProviderSetting = copy(models = models.toMutableList().apply {
            val model = removeAt(from); add(to, model)
        })
        override fun copyProvider(id: Uuid, enabled: Boolean, name: String, models: List<Model>, builtIn: Boolean, description: @Composable (() -> Unit), shortDescription: @Composable (() -> Unit)): ImageProviderSetting {
            return this.copy(id = id, enabled = enabled, name = name, models = models, builtIn = builtIn, description = description, shortDescription = shortDescription)
        }
    }

    @Serializable
    @SerialName("swarm")
    data class Swarm(
        override var id: Uuid = Uuid.random(),
        override var enabled: Boolean = true,
        override var name: String = "Swarm",
        override var models: List<Model> = emptyList(),
        @Transient override val builtIn: Boolean = false,
        @Transient override val description: @Composable (() -> Unit) = {},
        @Transient override val shortDescription: @Composable (() -> Unit) = {},
        var apiKey: String = "",
        var baseUrl: String = "",
    ) : ImageProviderSetting() {
        override fun addModel(model: Model): ImageProviderSetting = copy(models = models + model)
        override fun editModel(model: Model): ImageProviderSetting = copy(models = models.map { if (it.id == model.id) model.copy() else it })
        override fun delModel(model: Model): ImageProviderSetting = copy(models = models.filter { it.id != model.id })
        override fun moveMove(from: Int, to: Int): ImageProviderSetting = copy(models = models.toMutableList().apply {
            val model = removeAt(from); add(to, model)
        })
        override fun copyProvider(id: Uuid, enabled: Boolean, name: String, models: List<Model>, builtIn: Boolean, description: @Composable (() -> Unit), shortDescription: @Composable (() -> Unit)): ImageProviderSetting {
            return this.copy(id = id, enabled = enabled, name = name, models = models, builtIn = builtIn, description = description, shortDescription = shortDescription)
        }
    }

    companion object {
        val Types by lazy {
            listOf(
                OpenAI::class,
                Google::class,
                Claude::class,
                Automatic1111::class,
                ComfyUI::class,
                Swarm::class,
            )
        }
    }
}
