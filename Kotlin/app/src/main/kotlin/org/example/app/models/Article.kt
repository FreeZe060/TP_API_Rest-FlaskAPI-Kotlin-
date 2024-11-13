package com.example.app.models

data class Article(
    val article_id: Int,
    val title: String,
    val slug: String,
    val content: String,
    val author: String
)
