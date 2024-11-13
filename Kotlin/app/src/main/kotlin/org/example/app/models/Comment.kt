package com.example.app.models

data class Comment(
    val comment_id: Int,
    val content: String,
    val author: String,
    val article_id: Int
)
