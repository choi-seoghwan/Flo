package com.example.flo.view

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.LyricsAdapter
import com.example.flo.R
import com.example.flo.base.BaseKotlinFragment
import com.example.flo.databinding.FragmentMainBinding
import com.example.flo.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_lyrics.*
import kotlinx.android.synthetic.main.layout_main_player_bottom.*
import kotlinx.android.synthetic.main.layout_main_player_track.*
import kotlinx.android.synthetic.main.layout_mini_player.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LyricFragment : BaseKotlinFragment<FragmentMainBinding, MainViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_lyrics
    override val viewModel: MainViewModel by sharedViewModel()

    private val TAG = "LyricFragment"

    private val lyricsAdapter: LyricsAdapter by inject()

    override fun initStartView() {
        main_player_lyrics_controller.player = viewModel.musicPlayerLiveData.value
        lyrics_recyclerview.run {
            adapter = lyricsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun initDataBinding() {
        viewModel.musicResponseLiveData.observe(this, Observer { music ->
            main_player_lyrics_title.text = music.title
            main_player_lyrics_artist.text = music.singer
        })
        viewModel.musicLyricsLiveData.observe(this, Observer {
            it.forEach { lyrics ->
                lyricsAdapter.addLyricItem(lyrics.time, lyrics.lyric)
            }
            lyricsAdapter.notifyDataSetChanged()
        })
        viewModel.currentLyricsPositionData.observe(this, Observer { position ->
            lyricsAdapter.setCurrentPosition(position)
            lyrics_recyclerview.scrollToPosition(position + 4)
            lyricsAdapter.notifyDataSetChanged()
        })
    }

    override fun initAfterBinding() {
        main_player_lyrics_close.setOnClickListener {
            activity?.onBackPressed()
        }
        lyricsAdapter.setItemClickListener(object : LyricsAdapter.ItemClickListener {
            override fun onClick(time: Long) {
                if (lyrics_seek.isChecked) {
                    viewModel.setSelectLyricPosition(time)
                    lyricsAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = LyricFragment()
    }
}