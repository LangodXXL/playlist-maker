package com.solyakov.playlist.data.history

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchHistoryRepositoryImpl(val scope: CoroutineScope): SearchHistoryRepository {



    private val historyList = ArrayDeque<String>()

    private val _historyState = MutableStateFlow<List<String>>(emptyList())


    companion object {
        private const val MAX_HISTORY_SIZE = 4
    }

    override fun addToHistory(word: String) {
        scope.launch(Dispatchers.IO) {
            historyList.removeIf { it == word }
            historyList.addFirst(word)

            if (historyList.size > MAX_HISTORY_SIZE) {
                historyList.removeLast()
            }
            _historyState.value = historyList.toList()
        }
    }

    override fun getHistory(): Flow<List<String>> = _historyState.asStateFlow()


//    private val historyList = mutableListOf<String>()
//    private val _historyUpdates = MutableSharedFlow<Unit>()
//
//    fun getHistoryRequests(): Flow<List<String>> = historyList.toList()
//
//    fun notifyHistoryChanged() {
//        scope.launch(Dispatchers.IO) {
//            _historyUpdates.emit(Unit)
//        }
//    }
//    fun addToHistory(word: Word) {
//        historyList.add(word.word)
//        notifyHistoryChanged()
//    }
}
