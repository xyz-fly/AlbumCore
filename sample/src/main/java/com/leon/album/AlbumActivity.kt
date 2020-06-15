package com.leon.album

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.bumptech.glide.Glide
import com.leon.album.core.*
import com.leon.album.databinding.ActivityAlbumBinding

class AlbumActivity : AppCompatActivity() {

    private var directoryList: List<AlbumDirectory>? = null

    private lateinit var adapter: MediaPagedListAdapter

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(16)
        .setPageSize(16)
        .setPrefetchDistance(8)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.setOnClickListener {
            directoryList?.let {
                DirectorySpinner(this, Glide.with(this), it) { directory ->
                    binding.tvTitle.text = directory.bucketName
                    submitList(directory.bucketId)
                }.show(
                    binding.tvTitle,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels - 500
                )
            }
        }

        adapter = MediaPagedListAdapter(Glide.with(this))
        binding.recyclerView.adapter = adapter

        LoaderManager.getInstance(this).initLoader(0, null, object : DirectoryCallback(
            this,
            MediaTypeSelection.Builder().image().video().create()
        ) {
            override fun onLoadComplete(
                loader: Loader<Cursor>,
                list: List<AlbumDirectory>
            ) {

                val count = list.sumBy { it.count }

                val allDirectory = AlbumDirectory(
                    0L,
                    "All of Media",
                    "",
                    null,
                    count
                )

                val newList = arrayListOf(allDirectory)
                newList.addAll(list)
                directoryList = newList

                binding.tvTitle.text = newList[0].bucketName
                submitList(newList[0].bucketId)
            }
        })
    }

    private fun submitList(id: Long) {
        AlbumFactory.getAlbumDataSource(
            this,
            MediaTypeSelection.Builder().setBucketId(id).image().video().create()
        ).toLiveData(
            config = config,
            boundaryCallback = object : PagedList.BoundaryCallback<AlbumMedia>() {
            }).observe(this, Observer {
            adapter.submitList(null)
            adapter.submitList(it)
        })
    }
}