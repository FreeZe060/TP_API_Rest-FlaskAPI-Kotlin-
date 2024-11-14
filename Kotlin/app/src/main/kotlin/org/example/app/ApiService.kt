package com.example.app

import com.example.app.models.Article
import com.example.app.models.ArticleResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    @GET("articles")
    suspend fun getArticles(): ArticleResponse

    @POST("articles")
    suspend fun createArticle(@Body article: Article): Response<Article>
}
