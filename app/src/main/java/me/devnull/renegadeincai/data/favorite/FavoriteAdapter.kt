package me.devnull.renegadeincai.data.favorite

import me.devnull.renegadeincai.data.db.entity.FavoriteEntity
import me.devnull.renegadeincai.data.model.FavoriteType

interface FavoriteAdapter<T> {
    val type: FavoriteType

    fun buildRefKey(target: T): String

    fun buildFavoriteEntity(
        target: T,
        existing: FavoriteEntity? = null,
        now: Long = System.currentTimeMillis()
    ): FavoriteEntity
}
