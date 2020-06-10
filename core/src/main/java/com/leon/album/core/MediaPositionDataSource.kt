package com.leon.album.core

import android.content.Context
import android.database.Cursor
import com.leon.album.core.interal.AlbumUtils
import com.leon.album.core.interal.ContentResolverPositionDataSource
import com.leon.album.core.interal.Storage

class MediaPositionDataSource(
    context: Context,
    bucketId: Long,
    types: IntArray,
    sortOrder: String = Storage.DEFAULT_ORDER_BY
) : ContentResolverPositionDataSource<AlbumMedia>(
    context,
    Storage.URI,
    Storage.MEDIA_PROJECTION,
    AlbumUtils.getBucketIdAndMediaTypeSelection(bucketId, types),
    AlbumUtils.getBucketIdAndMediaTypeSelectionArgs(bucketId, types),
    sortOrder
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