package com.leon.album.core

import android.provider.MediaStore

class MediaTypeSelection {

    private var selectImage = false
    private var selectVideo = false

    fun media(): MediaTypeSelection {
        selectImage = true
        return this
    }

    fun video(): MediaTypeSelection {
        selectVideo = true
        return this
    }

    fun create(): IntArray {
        return when {
            selectImage and selectVideo ->
                intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

            selectImage -> intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

            selectVideo -> intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

            else -> intArrayOf()
        }
    }
}