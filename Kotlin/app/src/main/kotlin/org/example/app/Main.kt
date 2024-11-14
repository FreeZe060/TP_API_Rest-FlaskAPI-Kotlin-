package com.example.app

import com.example.app.models.Article
import com.example.app.models.ArticleResponse
import com.example.app.models.Comment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.PrintStream
import kotlinx.coroutines.*

fun searchArticleByName(apiService: ApiService, searchTerm: String) = runBlocking {
    try {
        val searchResults = apiService.searchArticles(content = searchTerm, author = null)
        if (searchResults.isNotEmpty()) {
            println("\nArticles trouvés pour le terme de recherche ' $searchTerm ' :\n")
            searchResults.forEach {
                println("ID: ${it.article_id}, Titre: ${it.title}, Auteur: ${it.author}")
            }
        } else {
            println("\nAucun article trouvé pour le terme de recherche : $searchTerm")
        }
    } catch (e: Exception) {
        println("Erreur lors de la recherche d'articles : ${e.message}")
    }
}

fun main() = runBlocking {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8000/")  // API Flask
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    System.setOut(PrintStream(System.out, true, "UTF-8"))

    println("\nBienvenue sur notre relation API !\n")
    
    try {
        val articleResponse: ArticleResponse = apiService.getArticles()
        
        val articles = articleResponse.data
        println("Articles récupérés :\n")
        articles.forEach { article ->
            println("ID: ${article.article_id}, Titre: ${article.title}, Auteur: ${article.author}\n")
        }

        searchArticleByName(apiService, "Operation billion still buy baby")

        // val newArticle = Article(0, "New Article Title", "This is the content", "slug-test", "Author Name")
        // val createdArticleResponse = apiService.createArticle(newArticle)
        // println("\nCréation d'un nouvel article :")
        // println("Article créé : ${createdArticleResponse.body()}\n")

        val articleId = 1  
        val retrievedArticle = apiService.getArticle(articleId)
        println("\nArticle récupéré :\nID: ${retrievedArticle.article_id}, Titre: ${retrievedArticle.title}\n")

        // val articleUpdatesFull = mapOf(
        //     "title" to "Nouveau titre",
        //     "content" to "Nouveau contenu",
        //     "slug" to "nouveau-slug",
        //     "author" to "Nouvel auteur"
        // )
        // val updatedArticleResponseFull = apiService.updateArticle(articleId, articleUpdatesFull)
        // if (updatedArticleResponseFull.isSuccessful) {
        //     println("\nArticle mis à jour : \n  ${updatedArticleResponseFull.body()}")
        // } else {
        //     println("Erreur lors de la mise à jour de l'article : ${updatedArticleResponseFull.errorBody()}")
        // }

        // val articleUpdatesPartial = mapOf(
        //     "content" to "Contenu modifié"
        // )
        // val updatedArticleResponsePartial = apiService.patchArticle(articleId, articleUpdatesPartial)
        // if (updatedArticleResponsePartial.isSuccessful) {
        //     println("\nArticle mis à jour : \n  ${updatedArticleResponsePartial.body()}")
        // } else {
        //     println("Erreur lors de la mise à jour partielle de l'article : ${updatedArticleResponsePartial.errorBody()}")
        // }


        // apiService.deleteArticle(articleId)
        // println("\nArticle avec ID $articleId supprimé\n")

        val comments = apiService.getCommentsForArticle(articleId)
        println("\nCommentaires pour l'article $articleId :")
        comments.forEach { comment ->
            println("\nID: ${comment.comment_id}, Contenu: ${comment.content}, Auteur: ${comment.author}")
        }

        // val newComment = Comment(0, "Nouveau commentaire", "Auteur de commentaire", articleId)
        // val createdCommentResponse = apiService.createComment(articleId, newComment)
        // println("\nCommentaire créé : \n ${createdCommentResponse.body()}")

        val commentId = 1
        val commentUpdatesFull = mapOf(
            "content" to "Contenu complètement modifié",  
            "author" to "Auteur complètement modifié"     
        )
        val updatedCommentResponseFull = apiService.putComment(commentId, commentUpdatesFull)
        if (updatedCommentResponseFull.isSuccessful) {
            println("\nCommentaire mis à jour : \n ${updatedCommentResponseFull.body()}")
        } else {
            println("Erreur lors de la mise à jour complète du commentaire : ${updatedCommentResponseFull.errorBody()}")
        }

        val commentUpdatesPartial = mapOf(
            "content" to "Contenu modifié",
        )
        val updatedCommentResponsePartial = apiService.patchComment(commentId, commentUpdatesPartial)
        if (updatedCommentResponsePartial.isSuccessful) {
            println("\nCommentaire mis à jour partiellement : \n ${updatedCommentResponsePartial.body()}")
        } else {
            println("Erreur lors de la mise à jour partielle du commentaire : ${updatedCommentResponsePartial.errorBody()}")
        }

        // apiService.deleteComment(articleId, commentId)
        // println("\nCommentaire avec ID $commentId pour l'article $articleId supprimé\n")

    } catch (e: Exception) {
        println("Erreur lors de l'appel API : ${e.message}")
    }
}