package com.leon.album.core.interal

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import androidx.core.os.OperationCanceledException
import androidx.paging.PositionalDataSource
import java.lang.ref.WeakReference

/**
 * @see [androidx.room.paging.LimitOffsetDataSource]
 */
abstract class ContentResolverPositionDataSource<T : Any>(
    private val context: Context,
    private val uri: Uri,
    private val projection: Array<String>,
    private val selection: String,
    private val selectionArgs: Array<String>,
    private val sortOrder: String,
    private val totalCount: Int = -1
) : PositionalDataSource<T>() {

    private var cancellationSignal: CancellationSignal? = null
    private var weakObserver = WeakReference(ForeLoadContentObserver(context))

    abstract fun convertRows(cursor: Cursor): List<T>

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        var list = emptyList<T>()

        cancellationSignal = CancellationSignal()

        val firstLoadPosition = computeInitialLoadPosition(params, totalCount)
        try {
            // bound the size requested, based on known count
            val firstLoadSize = computeInitialLoadSize(params, firstLoadPosition, totalCount)
            val limitOffsetSortOrder = getOffsetLimit(firstLoadPosition, firstLoadSize)

            context.contentResolver.registerContentObserver(uri, true, weakObserver.get() as ForeLoadContentObserver)

            val cursor: Cursor? = ContentResolverCompat.query(
                context.contentResolver,
                uri,
                projection,
                selection,
                selectionArgs,
                limitOffsetSortOrder,
                cancellationSignal
            )

            cursor?.use {
                list = convertRows(it)
            }
        } catch (e: Exception) {
            if (e is OperationCanceledException) {
                // ContentResolverCompat.query() can throw a framework OperationCanceledException, ContentResolverCompat.java:87
                // We catch that, then do nothing
            } else {
                // If it's not a framework OperationCanceledException, re-throw the exception
                throw e
            }
        } finally {
            cancellationSignal = null
        }

        val count = if (totalCount < 0) firstLoadPosition + list.size else totalCount
        callback.onResult(list, firstLoadPosition, count)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        var list = emptyList<T>()
        cancellationSignal = CancellationSignal()
        try {
            val startPosition = params.startPosition
            val loadCount = params.loadSize
            val limitOffsetSortOrder = getOffsetLimit(startPosition, loadCount)

            val cursor: Cursor? = ContentResolverCompat.query(
                context.contentResolver,
                uri,
                projection,
                selection,
                selectionArgs,
                limitOffsetSortOrder,
                cancellationSignal
            )
            cursor?.use {
                list = convertRows(it)
            }
        } finally {
            cancellationSignal = null
        }
        callback.onResult(list)
    }

    override fun invalidate() {
        cancellationSignal?.cancel()
        super.invalidate()
    }

    private fun getOffsetLimit(startPosition: Int, loadCount: Int) =
        "$sortOrder limit $loadCount offset $startPosition"

    inner class ForeLoadContentObserver(context: Context) : ContentObserver(Handler(context.mainLooper)) {

        override fun onChange(selfChange: Boolean) {
            invalidate()
        }

        override fun deliverSelfNotifications(): Boolean {
            return false
        }
    }
}