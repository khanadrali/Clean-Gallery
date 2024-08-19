package com.avrioc.cleangallery.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.avrioc.cleangallery.domain.model.Media

class MediaDiffCallback(
    private val oldList: List<Media>,
    private val newList: List<Media>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].uri == newList[newItemPosition].uri
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}