package rs.smobile.chucknorrisjokes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    private val jokeRepository: JokeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState>
        get() = _uiState

    init {
        fetchNewJoke()
    }

    fun fetchNewJoke() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            when (val response = jokeRepository.getJoke()) {
                is Resource.Success -> {
                    _uiState.value = MainUiState.Success(response.data)
                }
                is Resource.Error -> {
                    _uiState.value = MainUiState.Failure(response.message)
                }
            }
        }
    }

}