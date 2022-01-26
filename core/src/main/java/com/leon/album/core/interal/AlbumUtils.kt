package com.leon.album.core.interal

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

internal object AlbumUtils {
    fun getUri(id: Long, mimeType: String): Uri {
        val contentUri = when (mimeType) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Files.getContentUri("external")
        }

        return ContentUris.withAppendedId(contentUri, id)
    }

    fun getColumnIndexOrThrow(c: Cursor, name: String): Int {
        val index = c.getColumnIndex(name)
        return if (index >= 0) index
        else c.getColumnIndexOrThrow("`$name`")
    }
}
