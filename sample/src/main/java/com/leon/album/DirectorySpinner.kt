package com.leon.album

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import androidx.appcompat.widget.ListPopupWindow
import com.bumptech.glide.RequestManager
import com.leon.album.core.AlbumDirectory
import com.leon.album.databinding.ListItemDirectoryBinding
import java.util.*

class DirectorySpinner(
    context: Context,
    glide: RequestManager,
    private val list: List<AlbumDirectory>,
    private val click: (AlbumDirectory) -> Unit
) {

    private var popupWindow = ListPopupWindow(context)
    private var adapter: ListAdapter? = null

    init {
        popupWindow.isModal = true
        adapter = DirectoryAdapter(glide, list)
        popupWindow.setAdapter(adapter)
        popupWindow.setOnItemClickListener { _, _, position, _ ->
            popupWindow.dismiss()
            val directory = list[position]
            click.invoke(directory)
        }
    }

    fun show(anchorView: View, width: Int, height: Int) {
        popupWindow.anchorView = anchorView
        popupWindow.setContentWidth(width)
        popupWindow.height = height
        popupWindow.show()
    }

    private class DirectoryAdapter(
        private val glide: RequestManager,
        private val list: List<AlbumDirectory>
    ) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view =
                convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.list_item_directory, parent, false)

            val directory = list[position]
            val binding = ListItemDirectoryBinding.bind(view)

            directory.albumMedia?.let { it ->
                glide.load(it.uri).into(binding.image)
                binding.name.text =
                    "${directory.bucketName}\nw=${it.width}\nh=${it.height}\nsize=${it.size}\ndate=${getDate(it.dateAdded * 1000)}\ndur=${it.duration}"
            } ?: run {
                binding.name.text = directory.bucketName
            }

            binding.count.text = directory.count.toString()

            return view
        }

        private fun getDate(date: Long) =
            DateFormat.format("yyyy-MM-dd HH:mm:ss", Date(date))

        override fun getItem(position: Int) = null

        override fun getItemId(position: Int) = 0L

        override fun getCount() = list.size
    }
}
