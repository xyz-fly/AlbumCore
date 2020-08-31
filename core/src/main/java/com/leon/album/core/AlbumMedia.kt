package com.leon.album.core

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import com.leon.album.core.interal.AlbumUtils
import com.leon.album.core.interal.Storage

data class AlbumMedia(
    val id: Long,
    val name: String?,
    val mimeType: String,
    val uri: Uri,
    val dateAdded: Long,
    val size: Long,
    val width: Long, // maybe is zero
    val height: Long, // maybe is zero
    val duration: Long // maybe is zero
) {

    internal class MediaCursorBuilder(cursor: Cursor) {

        private val indexOfId = AlbumUtils.getColumnIndexOrThrow(cursor, BaseColumns._ID)
        private val indexOfDisplayName = AlbumUtils.getColumnIndexOrThrow(cursor, MediaStore.MediaColumns.DISPLAY_NAME)
        private val indexOfMimeType = AlbumUtils.getColumnIndexOrThrow(cursor, MediaStore.MediaColumns.MIME_TYPE)
        private val indexOfDateAdded = AlbumUtils.getColumnIndexOrThrow(cursor, MediaStore.MediaColumns.DATE_ADDED)
        private val indexOfSize = AlbumUtils.getColumnIndexOrThrow(cursor, MediaStore.MediaColumns.SIZE)
        private val indexOfWidth = AlbumUtils.getColumnIndexOrThrow(cursor, MediaStore.MediaColumns.WIDTH)
        private val indexOfHeight = AlbumUtils.getColumnIndexOrThrow(cursor, MediaStore.MediaColumns.HEIGHT)
        private val indexOfDuration = AlbumUtils.getColumnIndexOrThrow(cursor, Storage.COLUMN_DURATION)

        fun getMedia(cursor: Cursor): AlbumMedia {

            val id = cursor.getLong(indexOfId)
            val mimeType = cursor.getString(indexOfMimeType) ?: ""

            return AlbumMedia(
                id,
                cursor.getString(indexOfDisplayName),
                mimeType,
                AlbumUtils.getUri(id, mimeType),
                cursor.getLong(indexOfDateAdded),
                cursor.getLong(indexOfSize),
                cursor.getLong(indexOfWidth),
                cursor.getLong(indexOfHeight),
                cursor.getLong(indexOfDuration)
            )
        }
    }
}