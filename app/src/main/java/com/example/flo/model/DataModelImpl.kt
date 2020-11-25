package com.example.flo.model

import com.example.flo.model.response.MusicResponse
import com.example.flo.model.service.MusicSearchService
import io.reactivex.Single

class DataModelImpl(private val service: MusicSearchService):DataModel{

    override fun getData(): Single<MusicResponse> {
        return service.getMusic()
    }
}