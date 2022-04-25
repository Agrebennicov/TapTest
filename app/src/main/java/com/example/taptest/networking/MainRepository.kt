package com.example.taptest.networking

import com.example.taptest.data.SearchResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class MainRepository {
    fun searchVideos(
        query: String
    ): Result<SearchResult> = kotlin.runCatching {
        val doc: Document = Jsoup.connect("$BASE_URL/results?search_query=$query").get()
        return Result.success(SearchResult(doc.html()))
    }

    companion object {
        private const val BASE_URL = "https://www.youtube.com/"
    }
}
