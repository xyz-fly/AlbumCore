package com.leon.album.core.interal

import android.content.Context
import android.database.Cursor
import com.leon.album.core.AlbumMedia
import com.leon.album.core.MediaTypeSelection

internal class MediaPositionDataSource(
    context: Context,
    selection: MediaTypeSelection
) : ContentResolverPositionPagingSource<AlbumMedia>(
    context,
    Storage.URI,
    Storage.MEDIA_PROJECTION,
    selection.getSelection(),
    selection.getSelectionArgs(),
    selection.sortOrder
) {

    override fun convertRows(cursor: Cursor): List<AlbumMedia> {
        val res = ArrayList<AlbumMedia>(cursor.count)
        if (cursor.count > 0) {
            val builder = AlbumMedia.MediaCursorBuilder(cursor)
            while (cursor.moveToNext()) {
                res += builder.getMedia(cursor)
            }
        }
        return res
    }
}
