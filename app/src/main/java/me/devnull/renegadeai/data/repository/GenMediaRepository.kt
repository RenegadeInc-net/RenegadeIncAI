package me.devnull.renegadeai.data.repository

import androidx.paging.PagingSource
import me.devnull.renegadeai.data.db.dao.GenMediaDAO
import me.devnull.renegadeai.data.db.entity.GenMediaEntity

class GenMediaRepository(private val dao: GenMediaDAO) {
    fun getAllMedia(): PagingSource<Int, GenMediaEntity> = dao.getAll()

    suspend fun insertMedia(media: GenMediaEntity) = dao.insert(media)

    suspend fun deleteMedia(id: Int) = dao.delete(id)
}
