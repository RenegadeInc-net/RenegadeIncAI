package me.devnull.renegadeai.di

import me.devnull.renegadeai.data.files.FilesManager
import me.devnull.renegadeai.data.files.SkillManager
import me.devnull.renegadeai.data.repository.ConversationRepository
import me.devnull.renegadeai.data.repository.FavoriteRepository
import me.devnull.renegadeai.data.repository.FilesRepository
import me.devnull.renegadeai.data.repository.GenMediaRepository
import me.devnull.renegadeai.data.repository.MemoryRepository
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
