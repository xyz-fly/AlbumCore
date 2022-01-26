package com.leon.album.core

import android.content.Context
import androidx.paging.PagingSource
import com.leon.album.core.interal.MediaPositionDataSource

object AlbumFactory {

    fun getAlbumPagingSource(
        context: Context,
        selection: MediaTypeSelection
    ): PagingSource<Int, AlbumMedia> {
        return MediaPositionDataSource(context, selection)
    }
}
