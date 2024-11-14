package com.example.app.models

// Le modèle principal qui correspond à la réponse complète de l'API
data class ArticleResponse(
    val data: List<Article>,
    val links: PaginationLinks
)

// Classe pour les liens de pagination
data class PaginationLinks(
    val next: String?,
    val prev: String?,
    val self: String
)
