package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_main_image.view.*

class MainSearchRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    data class ImageItem(var imageUrl:String)

    class ImageHolder(parent:ViewGroup):RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_main_image, parent, false)) {
        fun onBind(item:ImageItem){
            itemView.run {
                Picasso.with(context).load(item.imageUrl).placeholder(R.drawable.ic_image_black_24dp).into(item_main_image_view)
            }
        }
    }

    private val imageItemList = ArrayList<ImageItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageHolder(parent)

    override fun getItemCount() = imageItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ImageHolder)?.onBind(imageItemList[position])
    }

    fun addImageItem(imageUrl: String){
        imageItemList.add(ImageItem(imageUrl))
    }
}