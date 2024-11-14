package com.example.app

import com.example.app.models.Article
import com.example.app.models.ArticleResponse
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
    try {
        // Appel à l'API pour récupérer les articles
        val articleResponse: ArticleResponse = apiService.getArticles()
        
        // Accéder à la liste des articles à partir de l'objet ArticleResponse
        val articles = articleResponse.data
        println("Articles récupérés :")
        articles.forEach { article ->
            println("ID: ${article.article_id}, Titre: ${article.title}, Auteur: ${article.author}")
        }

        // Création d'un nouvel article
        val newArticle = Article(0, "New Article Title", "This is the content", "slug-test", "Author Name")
        val createdArticleResponse = apiService.createArticle(newArticle)
        println("Created Article: ${createdArticleResponse.body()}")

    } catch (e: Exception) {
        println("Erreur lors de l'appel API : ${e.message}")
    }
}