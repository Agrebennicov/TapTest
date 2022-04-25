package com.example.taptest.ui.main

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

data class MainState(
    val isLoading: Boolean = false,
    val dataFlow: Flow<PagingData<String>>? = null
)
