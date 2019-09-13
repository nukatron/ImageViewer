package com.nutron.imageviewer.presentation.imagelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nutron.imageviewer.R
import com.nutron.imageviewer.module.extdi.ImageLoader
import com.nutron.imageviewer.presentation.entity.ImageUiData

interface OnImageListItemClickListener {
    fun onImageItemClickListener(position: Int, data: ImageUiData, view: ImageView)
}

class ImageListAdapter(
    val imageLoader: ImageLoader,
    val extListener: OnImageListItemClickListener
) : RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>() {

    private val imageList = arrayListOf<ImageUiData>()

    fun updateData(newList: List<ImageUiData>) {
        val diffResult = DiffUtil.calculateDiff(ImageDiffUtilCallBack(newList, imageList))
        imageList.clear()
        this.imageList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_layout, parent, false)
        return ImageListViewHolder(view, imageLoader)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        holder.bindData(imageList[position])
        holder.view.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            extListener.onImageItemClickListener(adapterPosition, imageList[adapterPosition], holder.imageView)
        }
    }


    class ImageListViewHolder(
        val view: View,
        val imageLoader: ImageLoader
    ): RecyclerView.ViewHolder(view) {

        val imageView = view.findViewById<AppCompatImageView>(R.id.item_img)
        val description = view.findViewById<AppCompatTextView>(R.id.item_description)
        val profileImage = view.findViewById<AppCompatImageView>(R.id.item_user_profile_img)
        val userName = view.findViewById<AppCompatTextView>(R.id.item_user_name)
        val userAccount = view.findViewById<AppCompatTextView>(R.id.item_user_social_account)
        val likeCountTxt = view.findViewById<AppCompatTextView>(R.id.item_like_count)

        fun bindData(data: ImageUiData) {
            imageLoader.load(data.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView)

            imageLoader.load(data.userProfileThumbnail)
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(profileImage)

            userName.text = data.username
            userAccount.text = data.userSocialAccount
            likeCountTxt.text = data.likes.toString()
            if (data.description != null) {
                description.text = data.description
                description.visibility = View.VISIBLE
            } else {
                description.visibility = View.GONE
            }
        }
    }

    class ImageDiffUtilCallBack(val newList: List<ImageUiData>, val oldList: List<ImageUiData>): DiffUtil.Callback() {


        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newList[newItemPosition]
            val oldItem = oldList[oldItemPosition]
            return newItem.id == oldItem.id
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newList[newItemPosition]
            val oldItem = oldList[oldItemPosition]
            return  newItem == oldItem
        }

    }
}