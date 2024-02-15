package rs.smobile.chucknorrisjokes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.FETCH_INITIAL_JOKE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.FETCH_NEW_JOKE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CATEGORIES
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CATEGORIES_FETCHED
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CATEGORIES_FETCH_EXECUTED
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CATEGORIES_FETCH_FAILED
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CATEGORIES_FETCH_FAILED_MESSAGE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.JOKE_CREATED_AT
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.NEW_JOKE_FETCHED
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.NEW_JOKE_FETCH_FAILED
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.NEW_JOKE_FETCH_FAILURE_MESSAGE
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.TOGGLED_JOKE_CATEGORY
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.TOGGLE_JOKE_CATEGORY
import rs.smobile.chucknorrisjokes.analytics.AnalyticsService
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import rs.smobile.chucknorrisjokes.data.repository.JokeRepository
import rs.smobile.chucknorrisjokes.data.repository.Resource
import rs.smobile.chucknorrisjokes.ui.JokeUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class JokesViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _uiState = MutableStateFlow<JokeUiState>(
        JokeUiState.Loading(
            categories = emptyList(),
            selectedCategory = null
        )
    )
    val uiState: StateFlow<JokeUiState>
        get() = _uiState

    init {
        fetchInitialJoke()
        fetchJokeCategories()
    }

    fun fetchNewJoke() {
        analyticsService.logEvent(FETCH_NEW_JOKE)
        fetchJoke()
    }

    fun toggleJokeCategory(category: String) {
        analyticsService.logEvent(
            TOGGLE_JOKE_CATEGORY,
            hashMapOf(TOGGLED_JOKE_CATEGORY to category)
        )
        _uiState.value = if (_uiState.value.selectedCategory == category) {
            _uiState.value.copyWithSelectedCategory(selectedCategory = null)
        } else {
            _uiState.value.copyWithSelectedCategory(selectedCategory = category)
        }

        fetchNewJoke()
    }

    private fun fetchInitialJoke() {
        analyticsService.logEvent(FETCH_INITIAL_JOKE)
        fetchJoke()
    }

    private fun fetchJoke() {
        viewModelScope.launch {
            _uiState.value = JokeUiState.Loading(
                categories = _uiState.value.categories,
                selectedCategory = _uiState.value.selectedCategory
            )
            when (val response = jokeRepository.getJoke(_uiState.value.selectedCategory)) {
                is Resource.Success -> {
                    trackNewJokeSuccess(response.data)
                    _uiState.value = when (_uiState.value) {
                        is JokeUiState.Success -> (_uiState.value as JokeUiState.Success).copy(joke = response.data)
                        else -> JokeUiState.Success(
                            response.data,
                            _uiState.value.categories,
                            _uiState.value.selectedCategory
                        )
                    }
                }

                is Resource.Error -> {
                    trackNewJokeFailure(response.message)
                    _uiState.value =
                        JokeUiState.Failure(
                            categories = _uiState.value.categories,
                            selectedCategory = _uiState.value.selectedCategory,
                            message = response.message
                        )
                }
            }
        }
    }

    private fun fetchJokeCategories() {
        viewModelScope.launch {
            when (val response = jokeRepository.getJokeCategories()) {
                is Resource.Success -> {
                    val categories = response.data ?: emptyList()
                    trackFetchJokeCategoriesSuccess(categories)
                    _uiState.value = when (_uiState.value) {
                        is JokeUiState.Success -> (_uiState.value as JokeUiState.Success).copy(
                            categories = categories
                        )

                        else -> JokeUiState.Success(
                            null,
                            categories,
                            _uiState.value.selectedCategory
                        )
                    }
                }

                is Resource.Error -> {
                    trackFetchJokeCategoriesFailure(response.message)
                    _uiState.value = JokeUiState.Failure(
                        categories = _uiState.value.categories,
                        selectedCategory = _uiState.value.selectedCategory,
                        message = response.message
                    )
                }
            }
        }
    }

    private fun trackFetchJokeCategoriesSuccess(categories: List<String>) {
        val currentDateTime = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        analyticsService.logEvent(
            JOKE_CATEGORIES_FETCHED,
            hashMapOf(
                JOKE_CATEGORIES_FETCH_EXECUTED to currentDateTime,
                JOKE_CATEGORIES to categories.joinToString(",")
            )
        )
    }

    private fun trackFetchJokeCategoriesFailure(message: String?) {
        val currentDateTime = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        analyticsService.logEvent(
            JOKE_CATEGORIES_FETCH_FAILED,
            hashMapOf(
                JOKE_CATEGORIES_FETCH_EXECUTED to currentDateTime,
                JOKE_CATEGORIES_FETCH_FAILED_MESSAGE to (message ?: "")
            )
        )
    }

    private fun trackNewJokeSuccess(joke: Joke?) {
        analyticsService.logEvent(
            NEW_JOKE_FETCHED,
            hashMapOf(
                JOKE to (joke?.value ?: ""),
                JOKE_CREATED_AT to (joke?.createdAt ?: ""),
                JOKE_CATEGORIES to (joke?.categories?.joinToString(",") ?: "")
            )
        )
    }

    private fun trackNewJokeFailure(message: String?) {
        analyticsService.logEvent(
            NEW_JOKE_FETCH_FAILED,
            hashMapOf(NEW_JOKE_FETCH_FAILURE_MESSAGE to (message ?: ""))
        )
    }

}