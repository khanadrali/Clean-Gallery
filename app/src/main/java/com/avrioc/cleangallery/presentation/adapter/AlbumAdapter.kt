package com.avrioc.cleangallery.presentation.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.avrioc.cleangallery.domain.model.Album
import com.example.testgallaryapplication.databinding.ItemAlbumGridBinding
import com.example.testgallaryapplication.databinding.ItemAlbumLinearBinding
import com.example.testgallaryapplication.presentation.listeners.AlbumClickListener

class AlbumAdapter(
    private val mediaList: MutableList<Album>,
    private val onAlbumClickListener: AlbumClickListener,
    private val itemSize: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType = VIEW_TYPE_GRID
    override fun getItemViewType(position: Int): Int {
        return if (viewType == VIEW_TYPE_GRID) VIEW_TYPE_GRID
        else VIEW_TYPE_LINEAR
    }

    fun setViewType(viewType: Int) {
        this.viewType = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> {
                val binding =
                    ItemAlbumGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AlbumGridViewHolder(binding)
            }

            else -> {
                val binding = ItemAlbumLinearBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AlbumLinearViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album = mediaList[position]
        when (holder) {
            is AlbumGridViewHolder -> holder.bind(album)
            is AlbumLinearViewHolder -> holder.bind(album)
        }
    }

    override fun getItemCount(): Int = mediaList.size

    fun updateList(albumList: List<Album>) {
        if (albumList != mediaList) {
            mediaList.clear()
            mediaList.addAll(albumList)
        }

    }

    inner class AlbumGridViewHolder(private val itemAlbumBinding: ItemAlbumGridBinding) :
        RecyclerView.ViewHolder(itemAlbumBinding.root) {

        fun bind(album: Album) {
            itemAlbumBinding.cardView.updateLayoutParams {
                width = itemSize
                height = itemSize
            }
            itemAlbumBinding.model = album
            itemAlbumBinding.lottieView = itemAlbumBinding.lottieAnimation
            itemAlbumBinding.root.setOnClickListener {
                onAlbumClickListener.onAlbumClick(album)
            }
        }
    }

    inner class AlbumLinearViewHolder(private val itemAlbumBinding: ItemAlbumLinearBinding) :
        RecyclerView.ViewHolder(itemAlbumBinding.root) {

        fun bind(album: Album) {
            itemAlbumBinding.model = album
            itemAlbumBinding.lottieView = itemAlbumBinding.lottieAnimation
            itemAlbumBinding.root.setOnClickListener {
                onAlbumClickListener.onAlbumClick(album)
            }
        }
    }

    companion object {
        const val VIEW_TYPE_LINEAR = 1
        const val VIEW_TYPE_GRID = 2
    }
}