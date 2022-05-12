package com.dapoi.healthnewsapp.di

import android.content.Context
import com.dapoi.healthnewsapp.data.source.remote.ApiConfig
import com.dapoi.healthnewsapp.data.source.local.NewsDatabase
import com.dapoi.healthnewsapp.data.source.NewsRepository

object Injection {
    fun provideNewsRepository(context: Context): NewsRepository {
        val db = NewsDatabase.getDatabase(context)
        val apiService = ApiConfig.provideApiService()
        return NewsRepository(db, apiService)
    }
}