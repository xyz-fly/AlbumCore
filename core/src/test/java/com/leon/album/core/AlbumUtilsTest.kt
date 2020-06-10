package com.leon.album.core

import android.provider.MediaStore
import com.leon.album.core.interal.AlbumUtils
import com.leon.album.core.interal.Storage
import org.junit.Assert
import org.junit.Test

class AlbumUtilsTest {

    @Test
    fun getMediaTypeSelectionTest() {
        var types = MediaTypeSelection().media().video().create()
        var selection = AlbumUtils.getMediaTypeSelection(types)
        var expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)

        types = MediaTypeSelection().media().create()
        selection = AlbumUtils.getMediaTypeSelection(types)
        expect = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)

        types = MediaTypeSelection().video().create()
        selection = AlbumUtils.getMediaTypeSelection(types)
        expect = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)

        types = MediaTypeSelection().create()
        selection = AlbumUtils.getMediaTypeSelection(types)
        expect = "${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)
    }

    @Test
    fun getBucketIdAndMediaTypeSelectionTest() {
        var types = MediaTypeSelection().media().video().create()
        var bucketId = 1L
        var selection = AlbumUtils.getBucketIdAndMediaTypeSelection(bucketId, types)
        var expect =
            "${Storage.COLUMN_BUCKET_ID}=? and (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)

        types = MediaTypeSelection().media().create()
        bucketId = -1L
        selection = AlbumUtils.getBucketIdAndMediaTypeSelection(bucketId, types)
        expect = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)

        types = MediaTypeSelection().media().create()
        bucketId = 1L
        selection = AlbumUtils.getBucketIdAndMediaTypeSelection(bucketId, types)
        expect =
            "${Storage.COLUMN_BUCKET_ID}=? and ${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)

        types = MediaTypeSelection().create()
        bucketId = 1L
        selection = AlbumUtils.getBucketIdAndMediaTypeSelection(bucketId, types)
        expect = "${Storage.COLUMN_BUCKET_ID}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, selection)
    }
}