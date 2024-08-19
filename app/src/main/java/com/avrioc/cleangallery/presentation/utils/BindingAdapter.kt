package com.avrioc.cleangallery.presentation.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


@BindingAdapter("visibleOrGone")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

// Custom binding adapter to load images using Glide
@BindingAdapter("imageUri", "lottieAnimationView")
fun loadImage(
    imageView: ImageView,
    imageUri: Uri,
    lottieAnimationView: LottieAnimationView?
) {
    lottieAnimationView?.apply {
        visibility = View.VISIBLE
        playAnimation()
    }

    Glide.with(imageView.context)
        .load(imageUri)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                lottieAnimationView?.apply {
                    visibility = View.GONE
                    cancelAnimation()
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                lottieAnimationView?.apply {
                    visibility = View.GONE
                    cancelAnimation()
                }
                return false
            }
        }).into(imageView)
}