package com.example.flo.view

import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.adapter.MainLyricsAdapter
import com.example.flo.R
import com.example.flo.base.BaseKotlinActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.view.main.BrowserFragment
import com.example.flo.view.main.RecommendFragment
import com.example.flo.view.main.MyFragment
import com.example.flo.view.main.SearchFragment
import com.example.flo.viewmodel.MainViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import kotlinx.android.synthetic.main.fragment_lyrics.*
import kotlinx.android.synthetic.main.layout_main_player.*
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
        mainplayer_lyrics.run {
            adapter = lyricsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun initDataBinding() {

        viewModel.selectedMenu.observe(this, Observer { menuItem ->
            var fragment : Fragment = RecommendFragment()
            when (menuItem.itemId) {
                R.id.menu_navi_recommend -> {
                    fragment = RecommendFragment()
                }
                R.id.menu_navi_browser -> {
                    fragment = BrowserFragment()
                }
                R.id.menu_navi_search -> {
                    fragment = SearchFragment()
                }
                R.id.menu_navi_my -> {
                    fragment = MyFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(viewDataBinding.mainScreen.id, fragment).commit()
        })
        viewModel.statePlayer.observe(this,Observer{
            viewDataBinding.isSlidingUp = it
            if(it == true)
                viewDataBinding.mainPlayer.mainPlayerBottom.player = player
            else
                viewDataBinding.miniPlayer.miniPlayerController.player = player
        })
        viewModel.musicPlayerLiveData.observe(this, Observer { it ->
            player = it
        })
        viewModel.musicResponseLiveData.observe(this, Observer {
            it.let { music ->
                // mini
                viewDataBinding.miniPlayer.music = music
                viewDataBinding.mainPlayer.music = music

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
        viewDataBinding.mainBottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            viewModel.selectMenu(menuItem)
            true
        }
        viewDataBinding.mainBottomNavigation.selectedItemId = R.id.menu_navi_recommend
        viewDataBinding.slidingLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}
            override fun onPanelStateChanged(
                panel: View?,
                previousState: PanelState?,
                newState: PanelState
            ) {
                viewModel.setStatePlayer(newState)
            }
        })
        // Player
        viewModel.initPlayer(this.applicationContext)
        viewModel.setStatePlayer(PanelState.COLLAPSED)
        viewModel.getMusicSearch()

        main_player_lyrics_click.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
                replace(viewDataBinding.lyricScreen.id, LyricFragment.newInstance())
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

    }

}
