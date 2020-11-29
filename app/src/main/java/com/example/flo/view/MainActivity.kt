package com.example.flo.view

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.flo.R
import com.example.flo.base.BaseKotlinActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.view.ui.MainTabListener
import com.example.flo.viewmodel.MainViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main_player_track.*
import kotlinx.android.synthetic.main.layout_main_tab.*
import kotlinx.android.synthetic.main.layout_mini_player.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseKotlinActivity<ActivityMainBinding, MainViewModel>(), MainTabListener {
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    private val TAG = "MainActivity"

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun initStartView() {
        // 각각 Fragment를 나눠서 만들어야 하지만, 현재 구현하고 싶은 주 기능이 아니기 때문에 남긴다.
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(main_screen.id, MainFragment.newInstance()).commit()

        viewModel.selectTab("HOME")
        player = ExoPlayerFactory.newSimpleInstance(this.applicationContext)
        mini_player.player = player
        main_player.player = player
    }

    override fun initDataBinding() {
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
        viewModel.musicResponseLiveData.observe(this, Observer {
            it.let { music ->
                var mediaSource: MediaSource = buildMediaSource(Uri.parse(music.file))
                //준비
                player!!.prepare(mediaSource, true, false)
                //스타트, 스탑
                player!!.playWhenReady.and(playWhenReady)
                mini_player_title.text = music.title
                main_player_title.text = music.title
                mini_player_singer.text = music.singer
                main_player_singer.text = music.singer
                Picasso.with(this).load(music.image).placeholder(R.drawable.ic_image_black_24dp)
                    .into(
                        album_standard
                    )
            }
        })
    }

    override fun initAfterBinding() {
        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                if (slideOffset != 0.0f) {
                    main_tab.visibility = View.GONE
                    main_player.visibility = View.VISIBLE
                    mini_player.visibility = View.GONE
                    main_player_title.isSelected = true
                } else {
                    main_tab.visibility = View.VISIBLE
                    mini_player.visibility = View.VISIBLE
                    main_player.visibility = View.GONE
                    mini_player_title.isSelected = true
                }
                Log.i(TAG, "onPanelSlide, offset $slideOffset")
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: PanelState?,
                newState: PanelState
            ) {
                Log.i(TAG, "onPanelStateChanged $newState")
            }
        })
        viewModel.getMusicSearch()
        if(player != null) {
            player!!.addListener(object : Player.EventListener {
                override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {}
                override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_BUFFERING) {

                    } else if (playbackState == Player.STATE_READY) {
                    }
                }
                override fun onRepeatModeChanged(repeatMode: Int) {}
                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
                override fun onPlayerError(error: ExoPlaybackException) {}
                override fun onPositionDiscontinuity(reason: Int) {}
                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
                override fun onSeekProcessed() {}
            })
        }
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

    fun buildMediaSource(uri: Uri) : MediaSource{
        var userAgent:String = Util.getUserAgent(this, "project_name")
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(
            uri
        )
    }

}
