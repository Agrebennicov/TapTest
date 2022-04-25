package com.example.taptest.data

data class PagedFrame(
    val page: Int,
    val framesUrls: List<String>,
    val isLastPage: Boolean
)