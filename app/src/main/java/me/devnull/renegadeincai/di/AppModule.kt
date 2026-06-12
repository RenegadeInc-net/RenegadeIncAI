package me.devnull.renegadeincai.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.serialization.json.Json
import me.devnull.renegadeincai.highlight.Highlighter
import me.devnull.renegadeincai.AppScope
import me.devnull.renegadeincai.data.ai.AILoggingManager
import me.devnull.renegadeincai.data.ai.tools.LocalTools
import me.devnull.renegadeincai.data.event.AppEventBus
import me.devnull.renegadeincai.service.ChatService
import me.devnull.renegadeincai.utils.EmojiData
import me.devnull.renegadeincai.utils.EmojiUtils
import me.devnull.renegadeincai.utils.JsonInstant
import me.devnull.renegadeincai.utils.SoundEffectPlayer
import me.devnull.renegadeincai.utils.UpdateChecker
import me.devnull.renegadeincai.web.WebServerManager
import me.devnull.renegadeincai.tts.provider.TTSManager
import org.koin.dsl.module

val appModule = module {
    single<Json> { JsonInstant }

    single {
        Highlighter(get())
    }

    single {
        AppEventBus()
    }

    single {
        LocalTools(get(), get())
    }

    single {
        UpdateChecker(get())
    }

    single {
        AppScope()
    }

    single<EmojiData> {
        EmojiUtils.loadEmoji(get())
    }

    single {
        TTSManager(get())
    }

    single {
        Firebase.crashlytics
    }

    single {
        Firebase.remoteConfig
    }

    single {
        Firebase.analytics
    }

    single {
        SoundEffectPlayer(get())
    }

    single {
        AILoggingManager()
    }

    single {
        ChatService(
            context = get(),
            appScope = get(),
            settingsStore = get(),
            conversationRepo = get(),
            memoryRepository = get(),
            generationHandler = get(),
            templateTransformer = get(),
            providerManager = get(),
            localTools = get(),
            mcpManager = get(),
            filesManager = get(),
            skillManager = get()
        )
    }

    single {
        WebServerManager(
            context = get(),
            appScope = get(),
            chatService = get(),
            conversationRepo = get(),
            settingsStore = get(),
            filesManager = get()
        )
    }
}
