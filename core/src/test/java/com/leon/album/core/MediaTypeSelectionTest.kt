package com.leon.album.core

import android.provider.MediaStore
import com.leon.album.core.interal.Storage
import org.junit.Assert
import org.junit.Test

class MediaTypeSelectionTest {

    @Test
    fun getMediaTypeSelectionTest() {
        // select all media
        var mediaTypeSelection = MediaTypeSelection.Builder().image().video().create()
        var expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        var argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        // select image
        mediaTypeSelection = MediaTypeSelection.Builder().image().create()
        expect = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        argExpect = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        // select video
        mediaTypeSelection = MediaTypeSelection.Builder().video().create()
        expect = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        argExpect = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        // select all media and bucket id
        mediaTypeSelection = MediaTypeSelection.Builder().setBucketId(10L).image().video().create()
        expect =
            "${Storage.COLUMN_BUCKET_ID}=? and (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        argExpect = arrayOf(
            10L.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        // no select
        mediaTypeSelection = MediaTypeSelection.Builder().create()
        expect = "${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        argExpect = emptyArray()
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        // select all media filter format
        var imageFilter = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG, MimeType.IMAGE_WEBP)
        var videoFilter = arrayOf(MimeType.VIDEO_MP4, MimeType.VIDEO_AVI)
        mediaTypeSelection = MediaTypeSelection.Builder().image(imageFilter).video(videoFilter).create()
        expect =
            "((${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?)) or (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?))) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageFilter,
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            *videoFilter
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        // select all media filter image format
        imageFilter = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG, MimeType.IMAGE_WEBP)
        mediaTypeSelection = MediaTypeSelection.Builder().image(imageFilter).video().create()
        expect =
            "((${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?)) or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageFilter,
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        // select video filter format and bucket id
        videoFilter = arrayOf(MimeType.VIDEO_MP4, MimeType.VIDEO_AVI)
        mediaTypeSelection = MediaTypeSelection.Builder().setBucketId(10).video(videoFilter).create()
        expect =
            "${Storage.COLUMN_BUCKET_ID}=? and (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?)) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        argExpect = arrayOf(
            10L.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            *videoFilter
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())
        println(mediaTypeSelection)
    }
}