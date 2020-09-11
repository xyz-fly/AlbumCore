package com.leon.album

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.paging.*
import com.bumptech.glide.Glide
import com.leon.album.core.*
import com.leon.album.databinding.ActivityAlbumBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class AlbumActivity : AppCompatActivity() {

    private var directoryList: List<AlbumDirectory>? = null

    private lateinit var adapter: MediaPagedListAdapter

    private val config = PagingConfig(
        pageSize = 20,
        prefetchDistance = 20,
        initialLoadSize = 20
    )

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)
    private val albumMediaId = MutableLiveData<Long>()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val posts = flowOf(
        clearListCh.consumeAsFlow().map { PagingData.empty<AlbumMedia>() },
        albumMediaId.asFlow().flatMapLatest {
            Pager(config) {
                AlbumFactory.getAlbumPagingSource(
                    this,
                    MediaTypeSelection.Builder().setBucketId(it).image().video().create()
                )
            }.flow.onStart {
                // let clearListCh emit empty-list first
                delay(1L)
            }
        }
    ).flattenMerge(2)

    @ExperimentalCoroutinesApi
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

        lifecycleScope.launchWhenCreated {
            posts.collectLatest {
                adapter.submitData(it)
            }
        }

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
        clearListCh.offer(Unit)
        albumMediaId.value = id
    }
}