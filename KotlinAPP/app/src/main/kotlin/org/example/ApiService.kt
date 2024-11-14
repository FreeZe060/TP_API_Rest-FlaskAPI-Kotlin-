package com.example.app

import com.example.app.models.Article
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiService {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getArticles(): List<Article> = withContext(Dispatchers.IO) {
        // Remplacer cette URL par l'URL réelle de votre API Flask
        client.get<ArticleResponse>("http://127.0.0.1:8000/articles").data
    }
}
