package com.example.flo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flo.base.BaseKotlinViewModel
import com.example.flo.model.DataModel
import com.example.flo.model.response.MainTabResponse
import com.example.flo.model.response.MusicResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val model: DataModel) : BaseKotlinViewModel() {

    private val TAG = "MainViewModel"

    private val mutableSelectedTab = MutableLiveData<String>()
    val selectedTab: LiveData<String> get() = mutableSelectedTab

    fun selectTab(tab: String) {
        if(selectedTab.value != tab){
            mutableSelectedTab.value = tab
        }
    }

    private val _musicResponseLiveData = MutableLiveData<MusicResponse>()
    val musicResponseLiveData:LiveData<MusicResponse>
        get() = _musicResponseLiveData

    fun getMusicSearch() {
        addDisposable(model.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.run {
                    _musicResponseLiveData.postValue(this)
                }
            }, {
                Log.d(TAG, "response error, message : ${it.message}")
            }))
    }
}