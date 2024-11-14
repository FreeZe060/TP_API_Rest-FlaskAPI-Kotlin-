package com.example.app

import com.example.app.models.Article
import com.example.app.models.ArticleResponse
import com.example.app.models.Comment
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("articles")
    suspend fun getArticles(): ArticleResponse

    @POST("articles")
    suspend fun createArticle(@Body article: Article): Response<Article>

    @GET("articles/search")
    suspend fun searchArticles(@Query("content") content: String?, @Query("author") author: String?): List<Article>

    @GET("articles/{article_id}")
    suspend fun getArticle(@Path("article_id") articleId: Int): Article

    @PATCH("articles/{article_id}")
    suspend fun patchArticle(@Path("article_id") articleId: Int, @Body updates: Map<String, String>): Response<Article>

    @PUT("articles/{article_id}")
    suspend fun updateArticle(@Path("article_id") articleId: Int, @Body updates: Map<String, String>): Response<Article>

    @HEAD("articles/{article_id}")
    suspend fun headArticle(@Path("article_id") articleId: Int): Response<Void>

    @DELETE("articles/{article_id}")
    suspend fun deleteArticle(@Path("article_id") articleId: Int): Response<Void>

    // Comment endpoints
    @GET("comments")
    suspend fun getAllComments(): List<Comment>

    @GET("articles/{article_id}/comments")
    suspend fun getCommentsForArticle(@Path("article_id") articleId: Int): List<Comment>

    @POST("articles/{article_id}/comments")
    suspend fun createComment(@Path("article_id") articleId: Int, @Body comment: Comment): Response<Comment>

    @DELETE("comments/{comment_id}")
    suspend fun deleteComment(@Path("comment_id") commentId: Int): Response<Void>

    @PATCH("comments/{comment_id}")
    suspend fun patchComment(@Path("comment_id") commentId: Int, @Body updates: Map<String, String>): Response<Comment>

    @PUT("comments/{comment_id}")
    suspend fun putComment(@Path("comment_id") commentId: Int, @Body updates: Map<String, String>): Response<Comment>

    @HEAD("comments/{comment_id}")
    suspend fun headComment(@Path("comment_id") commentId: Int): Response<Void>
}
