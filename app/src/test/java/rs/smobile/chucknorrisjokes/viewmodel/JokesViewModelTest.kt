package rs.smobile.chucknorrisjokes.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import rs.smobile.chucknorrisjokes.AppDispatcherRule
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants
import rs.smobile.chucknorrisjokes.analytics.AnalyticsService
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import rs.smobile.chucknorrisjokes.data.repository.JokeRepository
import rs.smobile.chucknorrisjokes.data.repository.Resource
import rs.smobile.chucknorrisjokes.ui.JokeUiState

internal class JokesViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val appDispatcherRule = AppDispatcherRule()

    private lateinit var jokeRepository: JokeRepository
    private lateinit var analyticsService: AnalyticsService
    private lateinit var viewModel: JokesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testFetchJokeWithSuccessfulResponse(): Unit = runTest {
        val getJokeTestResource = Resource.Success(
            Joke(
                emptyList(),
                createdAt = "",
                iconUrl = "",
                id = "",
                updatedAt = "",
                url = "",
                value = ""
            )
        )
        val getJokeCategoriesResource = Resource.Success(emptyList<String>())
        initMocks(getJokeTestResource, getJokeCategoriesResource)
        advanceUntilIdle()

        verifyInitialState()

        viewModel.fetchNewJoke()
        advanceUntilIdle()

        verify { analyticsService.logEvent(AnalyticsConstants.FETCH_NEW_JOKE) }
        verifyFetchJokeSuccess(getJokeTestResource.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testFetchJokeWithFailureResponse() = runTest {
        val getJokeTestResource = Resource.Error<Joke>("test error")
        val getJokeCategoriesResource = Resource.Success(emptyList<String>())
        initMocks(getJokeTestResource, getJokeCategoriesResource)
        advanceUntilIdle()

        coEvery { jokeRepository.getJoke(category = null) } returns getJokeTestResource
        verifyInitialState()

        viewModel.fetchNewJoke()
        advanceUntilIdle()

        verify { analyticsService.logEvent(AnalyticsConstants.FETCH_NEW_JOKE) }
        verifyFetchJokeFailure(getJokeTestResource.message)
    }

    private fun initMocks(
        getJokeResource: Resource<Joke>,
        getJokeCategoriesResource: Resource<List<String>>
    ) {
        jokeRepository = mockk()
        coEvery { jokeRepository.getJoke(category = any()) } returns getJokeResource
        coEvery { jokeRepository.getJokeCategories() } returns getJokeCategoriesResource

        analyticsService = mockk(relaxed = true)
        viewModel = JokesViewModel(jokeRepository, analyticsService)
    }

    private fun verifyInitialState() {
        verify { analyticsService.logEvent(AnalyticsConstants.FETCH_INITIAL_JOKE) }
        coVerify(exactly = 1) { jokeRepository.getJoke(category = null) }
    }

    private fun verifyFetchJokeFailure(message: String?) {
        coVerify { jokeRepository.getJoke(category = null) }
        assert(viewModel.uiState.value is JokeUiState.Failure)
        assertEquals(message, (viewModel.uiState.value as JokeUiState.Failure).message)
        verifyLogNewJokeFetchFailed(message)
    }

    private fun verifyFetchJokeSuccess(joke: Joke?) {
        coVerify { jokeRepository.getJoke(category = null) }
        assert(viewModel.uiState.value is JokeUiState.Success)
        assertEquals(joke, (viewModel.uiState.value as JokeUiState.Success).joke)
        verifyLogNewJokeFetchedEvent(joke)
    }

    private fun verifyLogNewJokeFetchedEvent(joke: Joke?) = verify {
        analyticsService.logEvent(
            AnalyticsConstants.NEW_JOKE_FETCHED,
            hashMapOf(
                AnalyticsConstants.JOKE to (joke?.value ?: ""),
                AnalyticsConstants.JOKE_CREATED_AT to (joke?.createdAt ?: ""),
                AnalyticsConstants.JOKE_CATEGORIES to (joke?.categories?.joinToString(",") ?: "")
            )
        )
    }

    private fun verifyLogNewJokeFetchFailed(message: String?) = verify {
        analyticsService.logEvent(
            AnalyticsConstants.NEW_JOKE_FETCH_FAILED,
            hashMapOf(AnalyticsConstants.NEW_JOKE_FETCH_FAILURE_MESSAGE to (message ?: ""))
        )
    }

}