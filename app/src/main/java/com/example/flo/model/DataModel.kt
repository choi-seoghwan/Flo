package com.example.flo.model

import com.example.flo.model.response.MusicResponse
import io.reactivex.Single

interface DataModel {
    fun getData(): Single<MusicResponse>
}