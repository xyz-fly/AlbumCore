package com.leon.album.core

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.leon.album.core.interal.DirectoryCursorLoader

abstract class DirectoryCallback(
    private val context: Context,
    private val selection: MediaTypeSelection
) : LoaderManager.LoaderCallbacks<Cursor> {

    private var isLoadFinish = false

    abstract fun onLoadComplete(loader: Loader<Cursor>, list: List<AlbumDirectory>)

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        isLoadFinish = false
        return DirectoryCursorLoader(context, selection)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (!isLoadFinish) {
            isLoadFinish = true

            val list = arrayListOf<AlbumDirectory>()
            data?.use {
                val builder = AlbumDirectory.DirectoryCursorBuilder(it)
                while (it.moveToNext()) {
                    list += builder.getDirectory(it)
                }
            }
            onLoadComplete(loader, list)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}
