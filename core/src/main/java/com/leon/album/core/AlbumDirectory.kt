package com.leon.album.core

import android.database.Cursor
import com.leon.album.core.interal.AlbumUtils
import com.leon.album.core.interal.Storage

data class AlbumDirectory(
    val bucketId: Long,
    val bucketName: String?,
    val mimeType: String,
    val albumMedia: AlbumMedia?,
    val count: Int
) {

    internal class DirectoryCursorBuilder(cursor: Cursor) {
        private val indexOfBucketId = AlbumUtils.getColumnIndexOrThrow(cursor, Storage.COLUMN_BUCKET_ID)
        private val indexOfBucketDisplayName =
            AlbumUtils.getColumnIndexOrThrow(cursor, Storage.COLUMN_BUCKET_DISPLAY_NAME)
        private val indexOfCount = AlbumUtils.getColumnIndexOrThrow(cursor, Storage.COLUMN_COUNT)

        private val builder = AlbumMedia.MediaCursorBuilder(cursor)

        fun getDirectory(cursor: Cursor): AlbumDirectory {
            val media = builder.getMedia(cursor)

            return AlbumDirectory(
                cursor.getLong(indexOfBucketId),
                cursor.getString(indexOfBucketDisplayName),
                media.mimeType,
                media,
                cursor.getInt(indexOfCount)
            )
        }
    }
}
