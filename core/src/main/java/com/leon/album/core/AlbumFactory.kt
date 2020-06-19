package com.leon.album.core

import android.content.Context
import androidx.paging.DataSource
import androidx.paging.DataSource.Factory
import com.leon.album.core.interal.MediaPositionDataSource

object AlbumFactory {

    fun getAlbumDataSource(
        context: Context,
        selection: MediaTypeSelection
    ): Factory<Int, AlbumMedia> {
        return object : Factory<Int, AlbumMedia>() {
            override fun create(): DataSource<Int, AlbumMedia> {
                return MediaPositionDataSource(context, selection)
            }
        }
    }
}