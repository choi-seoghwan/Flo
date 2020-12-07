package com.example.flo.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flo.R
import com.example.flo.base.BaseKotlinViewModel
import com.example.flo.model.DataModel
import com.example.flo.model.response.LyricsInfo
import com.example.flo.model.response.MusicResponse
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_main_player.*


class MainViewModel(private val model: DataModel) : BaseKotlinViewModel() {

    // Main Tab
    private val TAG = "MainViewModel"

    // player
    private val _musicPlayerLiveData = MutableLiveData<SimpleExoPlayer>()
    val musicPlayerLiveData: LiveData<SimpleExoPlayer>
        get() = _musicPlayerLiveData

    fun initPlayer(context: Context) {
        _musicPlayerLiveData.postValue(SimpleExoPlayer.Builder(context).build())
    }

    // music
    private val _musicResponseLiveData = MutableLiveData<MusicResponse>()
    val musicResponseLiveData: LiveData<MusicResponse>
        get() = _musicResponseLiveData

    fun getMusicSearch() {
        addDisposable(
            model.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.run {
                        _musicResponseLiveData.postValue(this)
                    }
                }, {
                    Log.d(TAG, "response error, message : ${it.message}")
                })
        )
    }


    // lyrics
    private val _musicLyricsLiveData = MutableLiveData<ArrayList<LyricsInfo>>()
    val musicLyricsLiveData: LiveData<ArrayList<LyricsInfo>>
        get() = _musicLyricsLiveData

    fun setLyricInfoList(lyricString: String) {
        _musicLyricsLiveData.postValue(getDividedSections(lyricString))
    }

    private fun getDividedSections(data: String): ArrayList<LyricsInfo> {
        val infoList = ArrayList<LyricsInfo>()
        val sections = data.split("\n")
        for (section in sections) {
            val totalMilliSeconds = getMilliSecondsFromSection(section)
            val text = section.substring(11)
            val musicInfo = LyricsInfo(totalMilliSeconds, text)

            infoList.add(musicInfo)
        }
        return infoList
    }

    private fun getMilliSecondsFromSection(section: String): Long {
        val time = section.substring(1, 9)
        val times = time.split(":")

        val min = times[0].toLong() * 60 * 1000 // 1min = 60sec
        val sec = times[1].toLong() * 1000 // 1sec = 1000 ms
        val ms = times[2].toLong()

        return min + sec + ms // total time of milliseconds
    }

    // current lyrics
    private val _currentLyricsPositionData = MutableLiveData<Int>()
    val currentLyricsPositionData: LiveData<Int>
        get() = _currentLyricsPositionData

    fun setCurrentLyricsPosition(currentPosition: Int) {
        _currentLyricsPositionData.postValue(currentPosition)
    }

    // select lyrics
    private val _selectedLyricPosition = MutableLiveData<Long>()
    val selectedLyricPosition: LiveData<Long>
        get() = _selectedLyricPosition

    fun setSelectLyricPosition(time: Long) {
        _selectedLyricPosition.postValue(time)
    }

    internal fun buildMediaSource(uri: Uri, context: Context): MediaSource {
        var userAgent: String = Util.getUserAgent(context, "project_name")
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
            .createMediaSource(
                uri
            )
    }

    private val _selectedMenu = MutableLiveData<MenuItem>()
    val selectedMenu: LiveData<MenuItem>
        get() = _selectedMenu

    fun selectMenu(item: MenuItem) {
        if (selectedMenu.value != item) {
            _selectedMenu.postValue(item)
        }
    }

    private val _statePlayer = MutableLiveData<Boolean>()
    val statePlayer: LiveData<Boolean>
        get() = _statePlayer

    fun setStatePlayer(currentState: SlidingUpPanelLayout.PanelState) {
        // Collapse = False
        // Expend = True
        val state = currentState != SlidingUpPanelLayout.PanelState.COLLAPSED
        if (state != _statePlayer.value)
            _statePlayer.postValue(state)
    }

    companion object {
        @BindingAdapter("bind:imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
            Picasso.with(view.context).load(url).into(view)
        }
    }
}