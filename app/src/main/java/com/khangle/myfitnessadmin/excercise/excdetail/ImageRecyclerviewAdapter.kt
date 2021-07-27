package com.khangle.myfitnessadmin.excercise.excdetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.khangle.myfitnessadmin.R

class ImageRecyclerviewAdapter : RecyclerView.Adapter<ImageRecyclerviewAdapter.ImageViewHolder>() {
    var uriList =  listOf<Uri>()
    var urlList = listOf<String>()
    var isUseUri = true
    fun applyUriList(uriList: List<Uri>) {
        isUseUri = true
        this.uriList = uriList
        notifyDataSetChanged()
    }

    fun applyUrlList(urlList: List<String>) {
        isUseUri = false
        this.urlList = urlList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if(isUseUri) {
            holder.setImageUri(uriList.get(position))
        } else {
            // use url
            val get = urlList.get(position)
            holder.loadUrl(urlList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return if(isUseUri) uriList.size else urlList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.image_item)
        }
        fun loadUrl(urlString: String) {
            imageView.load(urlString) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }
        }
        fun setImageUri(uri: Uri) {
            imageView.setImageURI(uri)
        }
    }
}