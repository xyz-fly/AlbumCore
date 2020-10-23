package com.leon.album.core

import android.provider.MediaStore
import com.leon.album.core.interal.Storage
import java.util.concurrent.TimeUnit
import org.junit.Assert
import org.junit.Test

class MediaTypeSelectionTest {

    @Test
    fun mediaTypeSelectionNoSelectTest() {
        val mediaTypeSelection = MediaTypeSelection.Builder().create()
        val expect = "${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = emptyArray<String>()
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaTest() {
        val mediaTypeSelection = MediaTypeSelection.Builder().image().video().create()

        // test select
        val expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        // test select args
        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionImageTest() {
        val mediaTypeSelection = MediaTypeSelection.Builder().image().create()

        val expect = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionVideoTest() {
        // select video
        val mediaTypeSelection = MediaTypeSelection.Builder().video().create()
        val expect = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaWithBucketIdTest() {
        val bucketId = 10L
        val mediaTypeSelection = MediaTypeSelection.Builder().setBucketId(bucketId).image().video().create()

        // test select
        val expect =
            "${Storage.COLUMN_BUCKET_ID}=? and (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        // test select args
        val argExpect = arrayOf(
            bucketId.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaFilterTest() {
        val imageFilter = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG, MimeType.IMAGE_WEBP)
        val videoFilter = arrayOf(MimeType.VIDEO_MP4, MimeType.VIDEO_AVI)
        val mediaTypeSelection = MediaTypeSelection.Builder().image(imageFilter).video(videoFilter).create()
        val expect =
            "((${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?)) or (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?))) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageFilter,
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            *videoFilter
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaImageFilterTest() {
        val imageFilter = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG, MimeType.IMAGE_WEBP)
        val mediaTypeSelection = MediaTypeSelection.Builder().image(imageFilter).video().create()
        val expect =
            "((${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?)) or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageFilter,
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaVideoFilterTest() {
        val videoFilter = arrayOf(MimeType.VIDEO_MP4, MimeType.VIDEO_AVI)
        val mediaTypeSelection = MediaTypeSelection.Builder().image().video(videoFilter).create()
        val expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?))) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            *videoFilter
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionImageFilterTest() {
        val imageFilter = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG, MimeType.IMAGE_WEBP)
        val mediaTypeSelection = MediaTypeSelection.Builder().image(imageFilter).create()
        val expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?)) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageFilter
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionVideoFilterTest() {
        val videoFilter = arrayOf(MimeType.VIDEO_MP4, MimeType.VIDEO_AVI)
        val mediaTypeSelection = MediaTypeSelection.Builder().video(videoFilter).create()
        val expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}=? or ${MediaStore.Files.FileColumns.MIME_TYPE}=?)) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            *videoFilter
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaIgnoreTest() {
        val imageIgnore = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG)
        val videoIgnore = arrayOf(MimeType.VIDEO_AVI)
        val mediaTypeSelection =
            MediaTypeSelection.Builder().image(ignore = imageIgnore).video(ignore = videoIgnore).create()
        val expect =
            "((${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}!=? and ${MediaStore.Files.FileColumns.MIME_TYPE}!=?)) or (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.Files.FileColumns.MIME_TYPE}!=?)) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageIgnore,
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            *videoIgnore
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaImageIgnoreTest() {
        val imageIgnore = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG)
        val mediaTypeSelection = MediaTypeSelection.Builder().image(ignore = imageIgnore).video().create()
        val expect =
            "((${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}!=? and ${MediaStore.Files.FileColumns.MIME_TYPE}!=?)) or ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageIgnore,
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionAllMediaVideoIgnoreTest() {
        val videoIgnore = arrayOf(MimeType.VIDEO_AVI)
        val mediaTypeSelection = MediaTypeSelection.Builder().image().video(ignore = videoIgnore).create()
        val expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? or (${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and ${MediaStore.Files.FileColumns.MIME_TYPE}!=?)) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            *videoIgnore
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaTypeSelectionImageIgnoreTest() {
        val imageIgnore = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG)
        val mediaTypeSelection = MediaTypeSelection.Builder().image(ignore = imageIgnore).create()
        val expect =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? and (${MediaStore.Files.FileColumns.MIME_TYPE}!=? and ${MediaStore.Files.FileColumns.MIME_TYPE}!=?)) and ${MediaStore.MediaColumns.SIZE}>0"
        Assert.assertEquals(expect, mediaTypeSelection.getSelection())

        val argExpect = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            *imageIgnore
        )
        Assert.assertArrayEquals(argExpect, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }

    @Test
    fun mediaCustomSelectionTest() {
        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
        )
        var mediaTypeSelection = MediaTypeSelection.Builder().selection(selection, selectionArgs).create()
        Assert.assertEquals(selection, mediaTypeSelection.getSelection())
        Assert.assertArrayEquals(selectionArgs, mediaTypeSelection.getSelectionArgs())

        mediaTypeSelection = MediaTypeSelection.Builder().image().video().selection(selection, selectionArgs).create()
        Assert.assertEquals(selection, mediaTypeSelection.getSelection())
        Assert.assertArrayEquals(selectionArgs, mediaTypeSelection.getSelectionArgs())

        println(mediaTypeSelection)
    }
}