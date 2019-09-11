package com.nutron.imageviewer.extdi

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nutron.imageviewer.R


interface ImageLoader {
    fun into(url: String, imageView: ImageView)
}

class ImageLoaderImpl(val context: Context):
    ImageLoader {

    override fun into(url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .into(imageView)
    }

}