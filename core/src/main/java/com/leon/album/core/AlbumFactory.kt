package com.leon.album.core

import android.content.Context
import androidx.paging.DataSource
import androidx.paging.DataSource.Factory
import com.leon.album.core.interal.Storage

object AlbumFactory {

    fun getAlbumDataSource(
        context: Context,
        bucketId: Long,
        types: IntArray,
        sortOrder: String = Storage.DEFAULT_ORDER_BY
    ): Factory<Int, AlbumMedia> {
        return object : Factory<Int, AlbumMedia>() {
            override fun create(): DataSource<Int, AlbumMedia> {
                return MediaPositionDataSource(context, bucketId, types, sortOrder)
            }
        }
    }
}