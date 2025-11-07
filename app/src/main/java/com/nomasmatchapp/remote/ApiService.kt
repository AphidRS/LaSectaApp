package com.nomasmatchapp.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getPageContentAsString(@Url url: String): String
}
