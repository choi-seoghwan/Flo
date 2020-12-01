package com.example.flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import kotlinx.android.synthetic.main.item_player_lyrics.view.*

class MainLyricsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    data class LyricsItem(var time: Long, var lyric: String)

    private var currentPosition = 0

    class LyricsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_player_lyrics, parent, false)
    ) {
        fun onBind(item: LyricsItem, currentPosition: Boolean) {
            itemView.run {
                player_lyrics.text = item.lyric
                if (currentPosition) player_lyrics.setTextColor(resources.getColor(R.color.text_color_06))
                else player_lyrics.setTextColor(resources.getColor(R.color.text_color_08))
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
    }

    fun addLyricItem(time: Long, lyric: String) {
        lyricsItemList.add(LyricsItem(time, lyric))
    }

    internal fun setCurrentPosition(position: Int) {
        currentPosition = position
    }
}