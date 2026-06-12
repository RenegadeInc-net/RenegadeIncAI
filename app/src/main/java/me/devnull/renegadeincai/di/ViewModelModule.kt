package me.devnull.renegadeincai.di

import me.devnull.renegadeincai.ui.pages.assistant.AssistantVM
import me.devnull.renegadeincai.ui.pages.assistant.detail.AssistantDetailVM
import me.devnull.renegadeincai.ui.pages.backup.BackupVM
import me.devnull.renegadeincai.ui.pages.chat.ChatDrawerVM
import me.devnull.renegadeincai.ui.pages.chat.ChatVM
import me.devnull.renegadeincai.ui.pages.debug.DebugVM
import me.devnull.renegadeincai.ui.pages.developer.DeveloperVM
import me.devnull.renegadeincai.ui.pages.favorite.FavoriteVM
import me.devnull.renegadeincai.ui.pages.search.SearchVM
import me.devnull.renegadeincai.ui.pages.history.HistoryVM
import me.devnull.renegadeincai.ui.pages.stats.StatsVM
import me.devnull.renegadeincai.ui.pages.imggen.ImgGenVM
import me.devnull.renegadeincai.ui.pages.extensions.PromptVM
import me.devnull.renegadeincai.ui.pages.extensions.QuickMessagesVM
import me.devnull.renegadeincai.ui.pages.extensions.SkillDetailVM
import me.devnull.renegadeincai.ui.pages.extensions.SkillsVM
import me.devnull.renegadeincai.ui.pages.setting.SettingVM
import me.devnull.renegadeincai.ui.pages.share.handler.ShareHandlerVM
import me.devnull.renegadeincai.ui.pages.translator.TranslatorVM
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<ChatVM> { params ->
        ChatVM(
            id = params.get(),
            context = get(),
            settingsStore = get(),
            conversationRepo = get(),
            chatService = get(),
            updateChecker = get(),
            analytics = get(),
            filesManager = get(),
            favoriteRepository = get(),
        )
    }
    viewModelOf(::ChatDrawerVM)
    viewModelOf(::SettingVM)
    viewModelOf(::DebugVM)
    viewModelOf(::HistoryVM)
    viewModelOf(::AssistantVM)
    viewModel<AssistantDetailVM> {
        AssistantDetailVM(
            id = it.get(),
            settingsStore = get(),
            memoryRepository = get(),
            filesManager = get(),
            skillManager = get(),
        )
    }
    viewModelOf(::TranslatorVM)
    viewModel<ShareHandlerVM> {
        ShareHandlerVM(
            text = it.get(),
            settingsStore = get(),
        )
    }
    viewModelOf(::BackupVM)
    viewModelOf(::ImgGenVM)
    viewModelOf(::DeveloperVM)
    viewModelOf(::PromptVM)
    viewModelOf(::QuickMessagesVM)
    viewModelOf(::SkillsVM)
    viewModel<SkillDetailVM> {
        SkillDetailVM(
            skillManager = get(),
            application = get()
        )
    }
    viewModelOf(::FavoriteVM)
    viewModelOf(::SearchVM)
    viewModelOf(::StatsVM)
}
