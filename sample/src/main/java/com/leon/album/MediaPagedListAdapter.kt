package com.leon.album

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.leon.album.core.AlbumMedia
import com.leon.album.databinding.ListItemAlbumBinding
import java.util.*

class MediaPagedListAdapter(val glide: RequestManager) :
    PagedListAdapter<AlbumMedia, MediaPagedListAdapter.ItemViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            glide,
            ListItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    class ItemViewHolder(
        val glide: RequestManager,
        val binding: ListItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, albumMedia: AlbumMedia?) {
            albumMedia?.let {
                glide.load(it.uri).into(binding.image)
                binding.name.text = (position + 1).toString()
                binding.content.text =
                    "type=${albumMedia.mimeType}\nw=${albumMedia.width}\nh=${albumMedia.height}\nsize=${albumMedia.size}\ndate=${getDate(albumMedia.dateAdded * 1000)}\ndur=${albumMedia.duration}"

                binding.ivPlay.visibility = if (albumMedia.mimeType.contains("video")) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        private fun getDate(date: Long) =
            DateFormat.format("yyyy-MM-dd HH:mm:ss", Date(date))
    }

    companion object {
        val CALLBACK = object : ItemCallback<AlbumMedia>() {
            override fun areItemsTheSame(oldItem: AlbumMedia, newItem: AlbumMedia): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AlbumMedia, newItem: AlbumMedia): Boolean {
                return oldItem == newItem
            }
        }
    }
}