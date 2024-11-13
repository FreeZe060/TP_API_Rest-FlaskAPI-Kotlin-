package com.example.app.repository

import com.example.app.models.Article

import com.example.app.ApiService

class ArticleRepository(private val api: ApiService) {
    suspend fun getArticles() = api.getArticles()
    suspend fun createArticle(article: Article) = api.createArticle(article)
}
