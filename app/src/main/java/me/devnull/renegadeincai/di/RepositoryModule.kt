package me.devnull.renegadeincai.di

import me.devnull.renegadeincai.data.files.FilesManager
import me.devnull.renegadeincai.data.files.SkillManager
import me.devnull.renegadeincai.data.repository.ConversationRepository
import me.devnull.renegadeincai.data.repository.FavoriteRepository
import me.devnull.renegadeincai.data.repository.FilesRepository
import me.devnull.renegadeincai.data.repository.GenMediaRepository
import me.devnull.renegadeincai.data.repository.MemoryRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        ConversationRepository(get(), get(), get(), get(), get(), get())
    }

    single {
        MemoryRepository(get())
    }

    single {
        GenMediaRepository(get())
    }

    single {
        FilesRepository(get())
    }

    single {
        FavoriteRepository(get())
    }

    single {
        FilesManager(get(), get(), get())
    }

    single {
        SkillManager(get(), get())
    }
}
