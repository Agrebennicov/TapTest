package com.example.taptest.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.taptest.data.PagedFrame
import com.example.taptest.data.SearchResult
import com.example.taptest.networking.MainRepository
import com.example.taptest.util.FrameParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: MainRepository,
    private val parser: FrameParser
) : ViewModel() {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    internal fun onSearch(query: String?) {
        if (query.isNullOrBlank()) {
            updateState(_state.value.copy(isLoading = false, dataFlow = null))
        } else {
            updateState(_state.value.copy(isLoading = true))
            fetchData(query)
        }
    }

    private fun fetchData(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.searchVideos(query)
                    .onSuccess(::handleSuccess)
                    .onFailure(::handleError)
            }
        }
    }

    private fun handleSuccess(result: SearchResult) {
        updateState(
            _state.value.copy(
                isLoading = false,
                dataFlow = Pager(
                    PagingConfig(
                        pageSize = 10,
                        maxSize = 500,
                        initialLoadSize = 1,
                        enablePlaceholders = false,
                        prefetchDistance = 10
                    )
                ) { MainPagingSource(parseResult(result)) }
                    .flow
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
            )
        )
    }

    private fun handleError(exception: Throwable) {
        updateState(_state.value.copy(isLoading = false, dataFlow = null))
        // TODO Show something nice
    }

    private fun parseResult(result: SearchResult): Map<Int, PagedFrame> {
        val dataList = parser.getFramesFromRawHtml(result.rawHtml).chunked(10)
        val dataMap = hashMapOf<Int, PagedFrame>()
        dataList.forEachIndexed { index, list ->
            dataMap[index] = PagedFrame(
                index,
                list,
                index == dataList.lastIndex
            )
        }
        return dataMap
    }

    private fun updateState(newState: MainState) {
        _state.value = newState
    }
}

class MainViewModelFactory(
    private val repository: MainRepository,
    private val parses: FrameParser
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass
        .getConstructor(MainRepository::class.java, FrameParser::class.java)
        .newInstance(repository, parses)
}
