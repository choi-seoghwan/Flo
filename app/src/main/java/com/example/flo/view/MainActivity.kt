package com.example.flo.view

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.adapter.MainLyricsAdapter
import com.example.flo.R
import com.example.flo.base.BaseKotlinActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.viewmodel.MainViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_lyrics.*
import kotlinx.android.synthetic.main.layout_main_player.*
import kotlinx.android.synthetic.main.layout_main_player_bottom.*
import kotlinx.android.synthetic.main.layout_main_tab.*
import kotlinx.android.synthetic.main.layout_mini_player.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseKotlinActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    private val TAG = "MainActivity"

    private val lyricsAdapter: MainLyricsAdapter by inject()
    private lateinit var player: SimpleExoPlayer

    override fun initStartView() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(main_screen.id, MainFragment.newInstance()).commit()

        viewModel.selectTab("HOME")
        mainplayer_lyrics.run {
            adapter = lyricsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun initDataBinding() {

        // Main Tab Setting(home, browser, search, my)
        viewModel.selectedTab.observe(this, Observer {
            it.let { tab ->
                when (tab) {
                    "HOME" -> landingHome()
                    "BROWSER" -> landingBrowser()
                    "SEARCH" -> landingSearch()
                    "MY" -> landingMy()
                }
            }
        })

        // Music Player
        viewModel.musicPlayerLiveData.observe(this, Observer { it ->
            player = it
            if (sliding_layout.panelState == PanelState.COLLAPSED) {
                mini_player.player = player
                main_player_bottom.player = null
            } else {
                mini_player.player = null
                main_player_bottom.player = player
            }
            when (player.repeatMode) {
                ExoPlayer.REPEAT_MODE_OFF -> {
                    main_player_repeat.setImageResource(R.drawable.btn_main_player_repeat)
                }
                ExoPlayer.REPEAT_MODE_ALL -> {
                    main_player_repeat.setImageResource(R.drawable.btn_main_player_repeat_1)
                }
                ExoPlayer.REPEAT_MODE_ONE -> {
                    main_player_repeat.setImageResource(R.drawable.btn_main_player_repeat_n)
                }
            }
            main_player_shuffle.isSelected = player.shuffleModeEnabled
        })

        // Music Data setting( title, artist, album-image, lyrics, player(main, mini) )
        viewModel.musicResponseLiveData.observe(this, Observer {
            it.let { music ->
                // mini
                mini_player_title.text = music.title
                mini_player_singer.text = music.singer

                // main
                main_player_title.text = music.title
                main_player_singer.text = music.singer
                Picasso.with(this).load(music.image).into(album_standard)

                //가사
                viewModel.setLyricInfoList(music.lyrics)
                if (music.lyrics.isNotEmpty()) main_player_lyrics_click.text = ""

                //뮤직 플레이어
                val mediaSource: MediaSource =
                    viewModel.buildMediaSource(Uri.parse(music.file), this)
                player.setMediaSource(mediaSource)
                player.prepare()
            }
        })

        // Current Position.
        main_player_bottom.setProgressUpdateListener { position, _ ->
            viewModel.musicLyricsLiveData.observe(this, Observer { lyrics ->
                for (i in 0 until lyrics.size - 1) {
                    if (position <= lyrics[i + 1].time) {
                        viewModel.setCurrentLyricsPosition(i)
                        break
                    }
                }
            })
        }

        // Jump to Position
        viewModel.selectedLyricPosition.observe(this, Observer { time ->
            player.seekTo(time)
        })

        // Add Main player Lyrics
        viewModel.musicLyricsLiveData.observe(this, Observer {
            it.forEach { lyrics -> lyricsAdapter.addLyricItem(lyrics.time, lyrics.lyric) }
            lyricsAdapter.notifyDataSetChanged()
        })

        // Scroll Main Player Lyrics
        viewModel.currentLyricsPositionData.observe(this, Observer { position ->
            lyricsAdapter.setCurrentPosition(position)
            mainplayer_lyrics.scrollToPosition(position + 1)
            lyricsAdapter.notifyDataSetChanged()
        })
    }

    override fun initAfterBinding() {

        // Player
        viewModel.initPlayer(this.applicationContext)

        // Main Tab Click.
        landingTabClickListener()

        // Get Music
        viewModel.getMusicSearch()

        // Mini-Player Slide to Main-Player
        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}
            override fun onPanelStateChanged(
                panel: View?,
                previousState: PanelState?,
                newState: PanelState
            ) {
                if (newState == PanelState.COLLAPSED) {
                    main_tab.visibility = View.VISIBLE
                    mini_player.visibility = View.VISIBLE
                    main_player.visibility = View.GONE
                    mini_player_title.isSelected = true
                } else {
                    main_tab.visibility = View.GONE
                    main_player.visibility = View.VISIBLE
                    mini_player.visibility = View.GONE
                    main_player_title.isSelected = true
                }
                Log.d(TAG, "sliding panel state : $newState")
                viewModel.setPlayer(player)
            }
        })
        main_player_lyrics_click.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
                replace(lyric_screen.id, LyricFragment.newInstance())
                addToBackStack(null)
            }
            transaction.commit()
        }
        main_player_like.setOnClickListener {
            main_player_like.isSelected = !main_player_like.isSelected
        }
        main_player_dislike.setOnClickListener {
            main_player_dislike.isSelected = !main_player_dislike.isSelected
        }
        main_player_repeat.setOnClickListener {
            when (player.repeatMode) {
                ExoPlayer.REPEAT_MODE_OFF -> {
                    player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
                }
                ExoPlayer.REPEAT_MODE_ALL -> {
                    player.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                }
                ExoPlayer.REPEAT_MODE_ONE -> {
                    player.repeatMode = ExoPlayer.REPEAT_MODE_OFF
                }
            }
            viewModel.setPlayer(player)
        }
        main_player_shuffle.setOnClickListener {
            player.shuffleModeEnabled = !player.shuffleModeEnabled
            viewModel.setPlayer(player)
        }
    }

    private fun landingTabClickListener() {
        mainTab_home_layout.setOnClickListener {
            viewModel.selectTab("HOME")
        }
        mainTab_browser_layout.setOnClickListener {
            viewModel.selectTab("BROWSER")
        }
        mainTab_search_layout.setOnClickListener {
            viewModel.selectTab("SEARCH")
        }
        mainTab_my_layout.setOnClickListener {
            viewModel.selectTab("MY")
        }
    }

    fun landingHome() {
        clearAllTab()
        mainTab_home_image.isSelected = true
        mainTab_home_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    fun landingBrowser() {
        clearAllTab()
        mainTab_browser_image.isSelected = true
        mainTab_browser_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    fun landingSearch() {
        clearAllTab()
        mainTab_search_image.isSelected = true
        mainTab_search_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    fun landingMy() {
        clearAllTab()
        mainTab_my_image.isSelected = true
        mainTab_my_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    private fun clearAllTab() {
        mainTab_home_image.isSelected = false
        mainTab_browser_image.isSelected = false
        mainTab_search_image.isSelected = false
        mainTab_my_image.isSelected = false
        mainTab_home_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
        mainTab_browser_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
        mainTab_search_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
        mainTab_my_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
    }

}
