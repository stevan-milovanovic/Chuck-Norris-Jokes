package rs.smobile.chucknorrisjokes.viewmodel

import io.mockk.*
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
        val testResource = Resource.Success(Joke(emptyList(), "", "", "", "", "", ""))
        initMocks(testResource)
        advanceUntilIdle()

        verifyInitialState()

        viewModel.fetchNewJoke()
        advanceUntilIdle()

        verify { analyticsService.logEvent(AnalyticsConstants.FETCH_NEW_JOKE) }
        verifyFetchJokeSuccess(testResource.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testFetchJokeWithFailureResponse() = runTest {
        val testResource = Resource.Error<Joke>("test error")
        initMocks(testResource)
        advanceUntilIdle()

        coEvery { jokeRepository.getJoke() } returns testResource
        verifyInitialState()

        viewModel.fetchNewJoke()
        advanceUntilIdle()

        verify { analyticsService.logEvent(AnalyticsConstants.FETCH_NEW_JOKE) }
        verifyFetchJokeFailure(testResource.message)
    }

    private fun initMocks(resource: Resource<Joke>) {
        jokeRepository = mockk()
        coEvery { jokeRepository.getJoke() } returns resource

        analyticsService = mockk(relaxed = true)
        viewModel = JokesViewModel(jokeRepository, analyticsService)
    }

    private fun verifyInitialState() {
        verify { analyticsService.logEvent(AnalyticsConstants.FETCH_INITIAL_JOKE) }
        coVerify(exactly = 1) { jokeRepository.getJoke() }
    }

    private fun verifyFetchJokeFailure(message: String?) {
        coVerify { jokeRepository.getJoke() }
        assert(viewModel.uiState.value is JokeUiState.Failure)
        assertEquals(message, (viewModel.uiState.value as JokeUiState.Failure).message)
        verifyLogNewJokeFetchFailed(message)
    }

    private fun verifyFetchJokeSuccess(joke: Joke?) {
        coVerify { jokeRepository.getJoke() }
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