package com.example.flo.view

import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.adapter.LyricsAdapter
import com.example.flo.R
import com.example.flo.base.BaseKotlinFragment
import com.example.flo.databinding.FragmentLyricsBinding
import com.example.flo.viewmodel.MainViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.fragment_lyrics.*
import kotlinx.android.synthetic.main.layout_main_player_bottom.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LyricFragment : BaseKotlinFragment<FragmentLyricsBinding, MainViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_lyrics
    override val viewModel: MainViewModel by sharedViewModel()

    private val TAG = "LyricFragment"

    private val lyricsAdapter: LyricsAdapter by inject()
    private lateinit var player: SimpleExoPlayer

    override fun initStartView() {
        lyrics_recyclerview.run {
            adapter = lyricsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun initDataBinding() {
        // Music Player.
        viewModel.musicPlayerLiveData.observe(this, Observer { it ->
            player = it
            main_player_lyrics_controller.player = player

            when (it.repeatMode) {
                ExoPlayer.REPEAT_MODE_OFF -> {
                    main_player_repeat.setImageResource(R.drawable.btn_main_player_repeat_n)
                }
                ExoPlayer.REPEAT_MODE_ALL -> {
                    main_player_repeat.setImageResource(R.drawable.btn_main_player_repeat)
                }
                ExoPlayer.REPEAT_MODE_ONE -> {
                    main_player_repeat.setImageResource(R.drawable.btn_main_player_repeat_1)
                }
            }
            main_player_shuffle.isSelected = player.shuffleModeEnabled
        })
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
        viewModel.selectedLyricPosition.observe(this, Observer {
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
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = LyricFragment()
    }
}