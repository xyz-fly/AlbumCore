package com.leon.album.core.interal

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import androidx.loader.content.CursorLoader
import com.leon.album.core.AlbumMedia
import com.leon.album.core.MediaTypeSelection

internal class DirectoryCursorLoader(
    context: Context,
    selection: MediaTypeSelection
) : CursorLoader(
    context,
    Storage.URI,
    Storage.DIRECTORY_PROJECTION,
    selection.getSelection(),
    selection.getSelectionArgs(),
    selection.sortOrder
) {

    override fun onLoadInBackground(): Cursor? {
        val cursor = super.onLoadInBackground()

        var totalCount = 0
        val map = LinkedHashMap<Long, Director>()

        val directoryCursor = MatrixCursor(Storage.DIRECTORY_COLUMNS)

        cursor?.let {
            val indexOfBucketId = AlbumUtils.getColumnIndexOrThrow(cursor, Storage.COLUMN_BUCKET_ID)
            val indexOfBucketDisplayName = AlbumUtils.getColumnIndexOrThrow(cursor, Storage.COLUMN_BUCKET_DISPLAY_NAME)

            val builder = AlbumMedia.MediaCursorBuilder(it)

            while (it.moveToNext()) {
                val bucketId = it.getLong(indexOfBucketId)

                if (map.containsKey(bucketId)) {
                    map[bucketId]?.let { director ->
                        director.count = director.count + 1
                    }
                } else {
                    map[bucketId] = Director(
                        bucketId,
                        it.getString(indexOfBucketDisplayName),
                        builder.getMedia(it),
                        1
                    )
                }
                totalCount++
            }
        }

        map.values.forEach {
            directoryCursor.addRow(
                arrayOf(
                    it.bucketId,
                    it.bucketName,
                    it.media.id,
                    it.media.name,
                    it.media.mimeType,
                    it.media.dateAdded,
                    it.media.size,
                    it.media.width,
                    it.media.height,
                    it.media.duration,
                    it.count
                )
            )
        }

        return directoryCursor
    }

    private class Director(
        val bucketId: Long,
        val bucketName: String,
        val media: AlbumMedia,
        var count: Int // sum of media
    )
}