package com.example.taptest.ui.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taptest.data.PagedFrame

class MainPagingSource(
    private val allFrames: Map<Int, PagedFrame>
) : PagingSource<Int, String>() {
    override fun getRefreshKey(state: PagingState<Int, String>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val pageToLoad = params.key ?: INITIAL_PAGE
        val currentPage = allFrames[pageToLoad]
        return LoadResult.Page(
            data = currentPage?.framesUrls ?: mutableListOf(),
            prevKey = if (pageToLoad > 1) pageToLoad - 1 else null,
            nextKey = if (currentPage?.isLastPage == true) null else pageToLoad + 1
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}
