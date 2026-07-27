package com.solyakov.playlist.data.history

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.solyakov.playlist.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class SearchHistoryRepositoryImplTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testScope = TestScope(mainDispatcherRule.testDispatcher + Job())

    private val dataStore by lazy {
        PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = {
                temporaryFolder.newFile("search_history.preferences_pb")
            }
        )
    }

    private val repository by lazy {
        SearchHistoryRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun `getHistory returns empty list by default`() = runTest {
        assertEquals(emptyList<String>(), repository.getHistory().first())
    }

    @Test
    fun `addToHistory adds query to start`() = runTest {
        repository.addToHistory("queen")

        assertEquals(listOf("queen"), repository.getHistory().first())
    }

    @Test
    fun `addToHistory moves duplicate query to start`() = runTest {
        repository.addToHistory("queen")
        repository.addToHistory("metallica")
        repository.addToHistory("queen")

        assertEquals(listOf("queen", "metallica"), repository.getHistory().first())
    }

    @Test
    fun `addToHistory ignores blank query`() = runTest {
        repository.addToHistory("   ")

        assertEquals(emptyList<String>(), repository.getHistory().first())
    }

    @Test
    fun `addToHistory stores at most ten items`() = runTest {
        repeat(12) { index ->
            repository.addToHistory("query_$index")
        }

        val history = repository.getHistory().first()

        assertEquals(10, history.size)
        assertEquals("query_11", history.first())
        assertEquals("query_2", history.last())
    }
}
