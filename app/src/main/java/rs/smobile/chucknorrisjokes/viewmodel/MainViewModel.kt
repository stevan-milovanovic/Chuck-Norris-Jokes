package rs.smobile.chucknorrisjokes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.FETCH_INITIAL_JOKE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.FETCH_NEW_JOKE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CATEGORIES
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CREATED_AT
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.NEW_JOKE_FETCH_FAILURE_MESSAGE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsService
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import rs.smobile.chucknorrisjokes.data.repository.JokeRepository
import rs.smobile.chucknorrisjokes.data.repository.Resource
import javax.inject.Inject

sealed class MainUiState {
    class Success(val joke: Joke?) : MainUiState()
    class Failure(val message: String?) : MainUiState()
    object Loading : MainUiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState>
        get() = _uiState

    init {
        fetchInitialJoke()
    }

    fun fetchNewJoke() {
        analyticsService.logEvent(FETCH_NEW_JOKE)
        fetchJoke()
    }

    private fun fetchInitialJoke() {
        analyticsService.logEvent(FETCH_INITIAL_JOKE)
        fetchJoke()
    }

    private fun fetchJoke() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            when (val response = jokeRepository.getJoke()) {
                is Resource.Success -> {
                    trackNewJokeSuccess(response.data)
                    _uiState.value = MainUiState.Success(response.data)
                }
                is Resource.Error -> {
                    trackNewJokeFailure(response.message)
                    _uiState.value = MainUiState.Failure(response.message)
                }
            }
        }
    }

    private fun trackNewJokeSuccess(joke: Joke?) {
        analyticsService.logEvent(
            AnalyticsConstants.NEW_JOKE_FETCHED,
            hashMapOf(
                JOKE to (joke?.value ?: ""),
                JOKE_CREATED_AT to (joke?.createdAt ?: ""),
                JOKE_CATEGORIES to (joke?.categories?.joinToString(",") ?: "")
            )
        )
    }

    private fun trackNewJokeFailure(message: String?) {
        analyticsService.logEvent(
            AnalyticsConstants.NEW_JOKE_FETCH_FAILED,
            hashMapOf(
                NEW_JOKE_FETCH_FAILURE_MESSAGE to (message ?: "")
            )
        )
    }

}