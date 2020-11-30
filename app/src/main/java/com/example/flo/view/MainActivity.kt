package com.example.flo.view

import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.MainLyricsAdapter
import com.example.flo.R
import com.example.flo.base.BaseKotlinActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.view.ui.MainTabListener
import com.example.flo.viewmodel.MainViewModel
import com.google.android.exoplayer2.source.MediaSource
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_lyrics.*
import kotlinx.android.synthetic.main.layout_main_player_bottom.*
import kotlinx.android.synthetic.main.layout_main_player_track.*
import kotlinx.android.synthetic.main.layout_main_tab.*
import kotlinx.android.synthetic.main.layout_mini_player.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseKotlinActivity<ActivityMainBinding, MainViewModel>(), MainTabListener {
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    private val TAG = "MainActivity"

    private val lyricsAdapter: MainLyricsAdapter by inject()

    override fun initStartView() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(main_screen.id, MainFragment.newInstance()).commit()
        viewModel.selectTab("HOME")

        viewModel.setPlayer(this.applicationContext)
        mainplayer_lyrics.run {
            adapter = lyricsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun initDataBinding() {

        // Main Tab setting(home, browser, search, my)
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

                //뮤직 플레이어
                var mediaSource: MediaSource = viewModel.buildMediaSource(
                    Uri.parse(music.file),
                    this
                )
                viewModel.musicPlayerLiveData.observe(this, Observer { it ->
                    it.let { player ->
                        mini_player.player = player
                        main_player_bottom.player = player
                        player.setMediaSource(mediaSource)
                        player.prepare()
                    }
                })
            }
        })

        // Current Music Position setting.
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

        // Jump to Same Music - Other Position
        viewModel.selectedLyricPosition.observe(this, Observer { time ->
            viewModel.musicPlayerLiveData.observe(this, Observer { player ->
                player.seekTo(time)
            })
        })

        // Add Main player Lyrics
        viewModel.musicLyricsLiveData.observe(this, Observer {
            it.forEach { lyrics ->
                lyricsAdapter.addLyricItem(lyrics.time, lyrics.lyric)
            }
            lyricsAdapter.notifyDataSetChanged()
        })

        // Scroll Main Player Lyrics
        viewModel.currentLyricsPositionData.observe(this, Observer { position ->
            main_player_lyrics_click.text = ""
            lyricsAdapter.setCurrentPosition(position)
            mainplayer_lyrics.scrollToPosition(position + 1)
            lyricsAdapter.notifyDataSetChanged()
        })
    }

    override fun initAfterBinding() {
        // Main Tab Click.
        landingTabClickListener()

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
                    viewModel.musicPlayerLiveData.observe(this@MainActivity, Observer { player ->
                        mini_player.player = player
                        main_player_bottom.player = null
                    })
                } else {
                    main_tab.visibility = View.GONE
                    main_tab.clearAnimation()
                    main_player.visibility = View.VISIBLE
                    mini_player.visibility = View.GONE
                    main_player_title.isSelected = true
                    viewModel.musicPlayerLiveData.observe(this@MainActivity, Observer { player ->
                        mini_player.player = null
                        main_player_bottom.player = player
                    })
                }
            }
        })
        viewModel.getMusicSearch()
        main_player_lyrics_click.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
                replace(lyric_screen.id, LyricFragment.newInstance())
                addToBackStack(null)
            }
            transaction.commit()
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

    override fun landingHome() {
        clearAllTab()
        mainTab_home_image.isSelected = true
        mainTab_home_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    override fun landingBrowser() {
        clearAllTab()
        mainTab_browser_image.isSelected = true
        mainTab_browser_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    override fun landingSearch() {
        clearAllTab()
        mainTab_search_image.isSelected = true
        mainTab_search_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    override fun landingMy() {
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
