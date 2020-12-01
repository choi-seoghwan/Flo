package com.example.flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import kotlinx.android.synthetic.main.item_lyrics_full.view.*

class LyricsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    data class LyricsItem(var time: Long, var lyric: String)

    private var currentPosition = 0

    class LyricsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_lyrics_full, parent, false)
    ) {
        fun onBind(item: LyricsItem, currentPosition: Boolean) {
            itemView.run {
                main_player_lyrics.text = item.lyric

                if (currentPosition) main_player_lyrics.setTextColor(resources.getColor(R.color.text_color_06))
                else main_player_lyrics.setTextColor(resources.getColor(R.color.text_color_08))
            }
        }
    }

    private val lyricsItemList = ArrayList<LyricsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LyricsViewHolder(parent)

    override fun getItemCount() = lyricsItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == currentPosition) (holder as? LyricsViewHolder)?.onBind(
            lyricsItemList[position],
            true
        )
        else (holder as? LyricsViewHolder)?.onBind(lyricsItemList[position], false)

        holder.itemView.setOnClickListener {
            itemClickListner.onClick(lyricsItemList[position].time)
        }
    }

    fun addLyricItem(time: Long, lyric: String) {
        lyricsItemList.add(LyricsItem(time, lyric))
    }

    internal fun setCurrentPosition(position: Int) {
        currentPosition = position
    }

    interface ItemClickListener {
        fun onClick(time: Long)
    }

    private lateinit var itemClickListner: ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}