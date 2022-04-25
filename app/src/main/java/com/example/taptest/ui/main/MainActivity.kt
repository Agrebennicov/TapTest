package com.example.taptest.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.taptest.databinding.ActivityMainBinding
import com.example.taptest.networking.MainRepository
import com.example.taptest.util.FrameParser
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter: MainAdapter = MainAdapter()
    private val viewModel: MainViewModel by viewModels(
        factoryProducer = {
            MainViewModelFactory(MainRepository(), FrameParser())
        }
    )
    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            viewModel.onSearch(query)
            return false
        }

        override fun onQueryTextChange(query: String?): Boolean {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                if (state.dataFlow != null) {
                    state.dataFlow.collectLatest { adapter.submitData(it) }
                }
                binding.progressBar.isVisible = state.isLoading
                binding.recyclerView.isVisible = !state.isLoading
            }
        }
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter
        binding.searchView.setOnQueryTextListener(queryTextListener)
    }
}
