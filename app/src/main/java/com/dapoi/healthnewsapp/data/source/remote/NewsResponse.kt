package com.dapoi.healthnewsapp.data.source.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class NewsResponse(

    @Json(name="totalResults")
	val totalResults: Int,

    @Json(name="articles")
	val articles: List<ArticlesItem>,

    @Json(name="status")
	val status: String
)

@Entity(tableName = "articles")
data class ArticlesItem(

	@PrimaryKey
	@Json(name="publishedAt")
	val publishedAt: String,

	@Json(name="author")
	val author: String,

	@Json(name="urlToImage")
	val urlToImage: String,

	@Json(name="description")
	val description: String,

	@Json(name="title")
	val title: String,

	@Json(name="url")
	val url: String,

	@Json(name="content")
	val content: String
)
