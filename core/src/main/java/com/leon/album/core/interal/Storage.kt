package com.leon.album.core.interal

import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore

internal object Storage {
    val URI: Uri = MediaStore.Files.getContentUri("external")
    const val COLUMN_BUCKET_ID = "bucket_id"
    const val COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name"
    const val COLUMN_COUNT = "count"
    const val COLUMN_DURATION = "duration"

    const val DEFAULT_ORDER_BY = "${MediaStore.MediaColumns.DATE_ADDED} desc"

    val MEDIA_PROJECTION = arrayOf(
        BaseColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.DATE_ADDED,
        MediaStore.MediaColumns.SIZE,
        MediaStore.MediaColumns.WIDTH, // require api 16
        MediaStore.MediaColumns.HEIGHT, // require api 16
        COLUMN_DURATION
    )

    val DIRECTORY_PROJECTION = Array(MEDIA_PROJECTION.size + 2) {
        when (it) {
            0 -> COLUMN_BUCKET_ID // require android Q
            1 -> COLUMN_BUCKET_DISPLAY_NAME // require android Q
            else -> MEDIA_PROJECTION[it - 2]
        }
    }

    // notice: this is the same to projection
    val DIRECTORY_COLUMNS = Array(DIRECTORY_PROJECTION.size + 1) {
        if (it < DIRECTORY_PROJECTION.size) {
            DIRECTORY_PROJECTION[it]
        } else {
            COLUMN_COUNT
        }
    }
}
