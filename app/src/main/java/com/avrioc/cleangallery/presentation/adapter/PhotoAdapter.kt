package com.avrioc.cleangallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avrioc.cleangallery.domain.model.Media
import com.example.testgallaryapplication.presentation.listeners.PhotoClickListener
import com.example.testgallaryapplication.databinding.ItemPhotoBinding

class PhotoAdapter(
    private val itemSize: Int,
    private val photoClickListener: PhotoClickListener,
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {


    private var mediaList: List<Media> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(mediaList[position])
    }

    override fun getItemCount(): Int = mediaList.size

    fun submitList(newList: List<Media>) {
        val oldList = mediaList
        val updatedList = oldList + newList
        val diffCallback = MediaDiffCallback(oldList, updatedList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mediaList = updatedList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class PhotoViewHolder(private val itemPhotoBinding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(itemPhotoBinding.root) {

        fun bind(media: Media) {
            itemPhotoBinding.cvPhoto.updateLayoutParams {
                width = itemSize
                height = itemSize
            }
            itemPhotoBinding.media = media
            itemPhotoBinding.lottieView = itemPhotoBinding.lottieAnimation
            itemPhotoBinding.root.setOnClickListener {
                photoClickListener.onPhotoClick(media)
            }
        }
    }
}
