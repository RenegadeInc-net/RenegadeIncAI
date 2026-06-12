package me.devnull.renegadeai.data.favorite

import me.devnull.renegadeai.data.db.entity.FavoriteEntity
import me.devnull.renegadeai.data.model.FavoriteType

interface FavoriteAdapter<T> {
    val type: FavoriteType

    fun buildRefKey(target: T): String

    fun buildFavoriteEntity(
        target: T,
        existing: FavoriteEntity? = null,
        now: Long = System.currentTimeMillis()
    ): FavoriteEntity
}
