package com.example.flo.model.service

import com.example.flo.model.response.MusicResponse
import io.reactivex.Single
import retrofit2.http.GET

interface MusicSearchService {
    @GET("/2020-flo/song.json")
    fun getMusic(): Single<MusicResponse>
}