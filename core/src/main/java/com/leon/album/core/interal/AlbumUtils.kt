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

    fun getMediaTypeSelection(args: IntArray): String {
        if (args.isEmpty()) {
            return "${MediaStore.MediaColumns.SIZE}>0"
        }

        val selection = if (args.size == 1) {
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
        } else {
            var s = "("
            args.forEachIndexed { index, _ ->
                s += if (index == args.size - 1) {
                    "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
                } else {
                    "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or "
                }
            }
            "$s)"
        }
        return "$selection and ${MediaStore.MediaColumns.SIZE}>0"
    }

    fun getMediaTypeSelectionArgs(args: IntArray): Array<String> {
        return args.map { it.toString() }.toTypedArray()
    }

    fun getBucketIdAndMediaTypeSelection(bucketId: Long, args: IntArray): String {
        return if (bucketId == -1L) {
            getMediaTypeSelection(args)
        } else {
            "${Storage.COLUMN_BUCKET_ID}=? and ${getMediaTypeSelection(args)}"
        }
    }

    fun getBucketIdAndMediaTypeSelectionArgs(bucketId: Long, args: IntArray): Array<String> {
        return if (bucketId == -1L) {
            getMediaTypeSelectionArgs(args)
        } else {
            Array(args.size + 1) {
                if (it == 0) {
                    bucketId.toString()
                } else {
                    args[it - 1].toString()
                }
            }
        }
    }
}