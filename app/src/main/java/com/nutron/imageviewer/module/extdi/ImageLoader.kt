package com.nutron.imageviewer.module.extdi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import java.io.File


interface ImageLoader {
    fun load(url: String?): ImageRequester
    fun load(@DrawableRes resId: Int): ImageRequester
    fun load(uri: Uri): ImageRequester
    fun load(file: File): ImageRequester
    fun loadAsBitmap(url: String?): ImageRequester
    fun loadAsBitmap(@DrawableRes resId: Int): ImageRequester
}

interface ImageRequester {
    fun placeholder(@DrawableRes resId: Int): ImageRequester
    fun placeHolder(drawable: Drawable): ImageRequester
    fun error(@DrawableRes resId: Int): ImageRequester
    fun fit(): ImageRequester
    fun centerCrop(): ImageRequester
    fun centerInside(): ImageRequester
    fun circleCrop(): ImageRequester
    fun resize(width: Int, height: Int): ImageRequester
    fun rounded(radius: Int): ImageRequester
    fun noFade(): ImageRequester
    fun crossFade(): ImageRequester
    @MainThread fun into(target: ImageView?)
    @MainThread fun getBitmap(listener: RequestListener<Bitmap>)
    @Throws(Exception::class) fun get(): Bitmap?
}

abstract class GlideImageRequester(
    private val context: Context,
    private var deferOptions: RequestOptions
): ImageRequester {

    open fun buildDrawable(view: View? = null): RequestBuilder<Drawable>? = null
    open fun buildBitmap(view: View? = null): RequestBuilder<Bitmap>? = null

    override fun placeholder(resId: Int): ImageRequester = also {
        deferOptions = deferOptions.placeholder(resId)
    }

    override fun placeHolder(drawable: Drawable): ImageRequester = also {
        deferOptions = deferOptions.placeholder(drawable)
    }

    override fun error(resId: Int): ImageRequester = also { deferOptions = deferOptions.error(resId) }

    override fun fit(): ImageRequester = also { deferOptions = deferOptions.fitCenter() }

    override fun centerCrop(): ImageRequester = also { deferOptions = deferOptions.centerCrop() }

    override fun centerInside(): ImageRequester = also { deferOptions = deferOptions.centerInside() }

    override fun circleCrop(): ImageRequester = also { deferOptions = deferOptions.circleCrop() }

    override fun resize(width: Int, height: Int): ImageRequester = also {
        deferOptions = deferOptions.override(width, height)
    }

    override fun rounded(radius: Int): ImageRequester = also {
        deferOptions = deferOptions.transform(RoundedCorners(radius))
    }

    override fun noFade(): ImageRequester = also {
        deferOptions = deferOptions.dontAnimate()
    }


    override fun into(target: ImageView?) {
        target?.let { view -> buildDrawable(view)?.into(view) }
    }

    override fun getBitmap(listener: RequestListener<Bitmap>) {
        buildBitmap()?.listener(listener)?.preload()
    }

    override fun get(): Bitmap? {
        return buildDrawable()?.submit()?.get()?.let { it as BitmapDrawable }?.bitmap
    }
}

class ImageDrawableRequester(
    private val context: Context,
    private var deferOptions: RequestOptions,
    private val buildRequest: (RequestManager) -> RequestBuilder<Drawable>
): GlideImageRequester(context, deferOptions) {

    private var transition: TransitionOptions<DrawableTransitionOptions, Drawable>? = null

    override fun crossFade(): ImageRequester = also {
        transition = DrawableTransitionOptions.withCrossFade()
    }

    override fun buildDrawable(view: View?): RequestBuilder<Drawable>? {
        return try {
            val glide = view?.let(Glide::with) ?: Glide.with(context)
            val request = buildRequest(glide).apply(deferOptions)
            transition?.let { request.apply { transition(it) } }
            request
        } catch (e: IllegalStateException) {
            null
        }
    }
}

class ImageBitmapRequester(
    private val context: Context,
    private var deferOptions: RequestOptions,
    private val buildRequest: (RequestManager) -> RequestBuilder<Bitmap>
): GlideImageRequester(context, deferOptions) {

    private var transition: TransitionOptions<BitmapTransitionOptions, Bitmap>? = null

    override fun crossFade(): ImageRequester = also {
        transition = BitmapTransitionOptions.withCrossFade()
    }

    override fun buildBitmap(view: View?): RequestBuilder<Bitmap>? {
        return try {
            val glide = view?.let(Glide::with) ?: Glide.with(context)
            val request = buildRequest(glide).apply(deferOptions)
            transition?.let { request.apply { transition(it) } }
            request
        } catch (e: IllegalStateException) {
            null
        }
    }
}

class ImageLoaderImpl(
    val context: Context
): ImageLoader {

    private fun defaultOptions(): RequestOptions {
        return RequestOptions()
            .format(DecodeFormat.PREFER_RGB_565)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }
    override fun load(url: String?): ImageRequester {
        return ImageDrawableRequester(
            context, defaultOptions()
        ) { glide -> glide.load(url?.takeIf { it.isNotEmpty() }) }
    }

    override fun load(resId: Int): ImageRequester {
        return ImageDrawableRequester(context, defaultOptions()) { it.load(resId) }
    }

    override fun load(uri: Uri): ImageRequester {
        return ImageDrawableRequester(context, defaultOptions()) { it.load(uri) }
    }

    override fun load(file: File): ImageRequester {
        return ImageDrawableRequester(context, defaultOptions()) { it.load(file) }
    }

    override fun loadAsBitmap(url: String?): ImageRequester {
        return ImageBitmapRequester(context, defaultOptions()) { glide ->
            glide.asBitmap().load(url?.takeIf { it.isNotEmpty() })
        }
    }

    override fun loadAsBitmap(resId: Int): ImageRequester {
        return ImageBitmapRequester(context, defaultOptions()) { glide ->
            glide.asBitmap().load(resId)
        }
    }

}