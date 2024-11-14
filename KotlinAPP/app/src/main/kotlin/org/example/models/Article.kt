package com.example.app.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    @SerialName("article_id") val articleId: Int,
    val author: String,
    val content: String,
    val slug: String,
    val title: String
)

@Serializable
data class ArticleResponse(
    val data: List<Article>,
    val links: Links
)

@Serializable
data class Links(
    val next: String?,
    val prev: String?,
    val self: String?
)
