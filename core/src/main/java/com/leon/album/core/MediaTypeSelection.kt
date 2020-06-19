package com.leon.album.core

import android.provider.MediaStore
import com.leon.album.core.interal.Storage

class MediaTypeSelection(
    private val bucketId: Long,
    private val map: HashMap<Int, Pair<Array<String>?, Array<String>?>>,
    val sortOrder: String
) {

    fun getSelection(): String {
        if (map.isEmpty()) {
            return "${MediaStore.MediaColumns.SIZE}>0"
        }

        var selection = ""

        map.values.forEachIndexed { index, pair ->
            var mediaSelection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
            // add filter
            val filter = pair.first
            filter?.takeIf { it.isNotEmpty() }?.let {
                var filterSelection = ""
                it.forEachIndexed { index, _ ->
                    filterSelection += if (index == it.size - 1) {
                        "${MediaStore.Files.FileColumns.MIME_TYPE}=?"
                    } else {
                        "${MediaStore.Files.FileColumns.MIME_TYPE}=? or "
                    }
                }

                filterSelection = if (it.size == 1) filterSelection else "($filterSelection)"
                mediaSelection = "($mediaSelection and $filterSelection)"
            }

            // add ignore
            val ignore = pair.second
            ignore?.takeIf { it.isNotEmpty() }?.let {
                var ignoreSelection = ""
                it.forEachIndexed { index, _ ->
                    ignoreSelection += if (index == it.size - 1) {
                        "${MediaStore.Files.FileColumns.MIME_TYPE}!=?"
                    } else {
                        "${MediaStore.Files.FileColumns.MIME_TYPE}!=? and "
                    }
                }

                ignoreSelection = if (it.size == 1) ignoreSelection else "($ignoreSelection)"
                mediaSelection = "($mediaSelection and $ignoreSelection)"
            }

            selection += if (index != map.size - 1) "$mediaSelection or " else mediaSelection
        }

        selection = if (map.size > 1) "($selection)" else selection

        return if (bucketId != 0L) {
            "${Storage.COLUMN_BUCKET_ID}=? and $selection and ${MediaStore.MediaColumns.SIZE}>0"
        } else {
            "$selection and ${MediaStore.MediaColumns.SIZE}>0"
        }
    }

    fun getSelectionArgs(): Array<String> {
        var args: Array<String> = arrayOf()

        if (bucketId != 0L) {
            args += arrayOf(bucketId.toString())
        }

        map.entries.forEach { entry ->
            args += arrayOf(entry.key.toString())

            // add filter args
            entry.value.first?.takeIf { it.isNotEmpty() }?.let {
                args += it
            }
            // add ignore args
            entry.value.second?.takeIf { it.isNotEmpty() }?.let {
                args += it
            }
        }
        return args
    }

    override fun toString(): String {
        return "selection = ${getSelection()} order by $sortOrder\nselectionArgs = ${getSelectionArgs().contentToString()}"
    }

    class Builder {
        private var bucketId = 0L
        private val map = HashMap<Int, Pair<Array<String>?, Array<String>?>>()
        private var sortOrder: String = Storage.DEFAULT_ORDER_BY

        fun setBucketId(bucketId: Long): Builder {
            this.bucketId = bucketId
            return this
        }

        fun image(filter: Array<String>? = null, ignore: Array<String>? = null): Builder {
            addMedia(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, filter, ignore)
            return this
        }

        fun video(filter: Array<String>? = null, ignore: Array<String>? = null): Builder {
            addMedia(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO, filter, ignore)
            return this
        }

        fun addMedia(mediaType: Int, filter: Array<String>? = null, ignore: Array<String>? = null): Builder {
            map[mediaType] = filter to ignore
            return this
        }

        fun sortOrder(sortOrder: String): Builder {
            this.sortOrder = sortOrder
            return this
        }

        fun create(): MediaTypeSelection {
            return MediaTypeSelection(bucketId, map, sortOrder)
        }
    }
}