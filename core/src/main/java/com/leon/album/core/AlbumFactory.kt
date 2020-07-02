package com.leon.album.core

import android.content.Context
import androidx.paging.DataSource
import androidx.paging.DataSource.Factory
import androidx.paging.PagingSource
import com.leon.album.core.interal.MediaPositionDataSource

object AlbumFactory {

    fun getAlbumPagingSource(
        context: Context,
        selection: MediaTypeSelection
    ): PagingSource<Int, AlbumMedia> {
        return object : Factory<Int, AlbumMedia>() {
            override fun create(): DataSource<Int, AlbumMedia> {
                return MediaPositionDataSource(context, selection)
            }
        }.asPagingSourceFactory().invoke()
    }
}