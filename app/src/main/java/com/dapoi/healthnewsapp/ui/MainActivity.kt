package com.dapoi.healthnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapoi.healthnewsapp.adapter.LoadingStateAdapter
import com.dapoi.healthnewsapp.adapter.NewsAdapter
import com.dapoi.healthnewsapp.databinding.ActivityMainBinding
import com.dapoi.healthnewsapp.viewmodel.MainViewModel
import com.dapoi.healthnewsapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
    }

    private fun getData() {
        val newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    newsAdapter.retry()
                }
            )
            setHasFixedSize(true)
        }
        mainViewModel = ViewModelFactory(this).create(MainViewModel::class.java)
        mainViewModel.news.observe(this) {
            newsAdapter.submitData(lifecycle, it)
        }
    }
}