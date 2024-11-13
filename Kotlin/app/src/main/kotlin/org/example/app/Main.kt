package com.example.app

import com.example.app.models.Article
import com.example.app.models.Comment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlinx.coroutines.*

fun main() = runBlocking {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8000/")  // API Flask
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    // Exemples d'appels API
    val articles = apiService.getArticles()
    println("Articles: $articles")

    val newArticle = Article(0, "New Article Title", "This is the content", "slug", "Author Name")
    val createdArticleResponse = apiService.createArticle(newArticle)
    println("Created Article: ${createdArticleResponse.body()}")
}
