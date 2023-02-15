package rs.smobile.chucknorrisjokes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import rs.smobile.chucknorrisjokes.data.repository.JokeRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val jokeRepository: JokeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Joke?>(null)
    val uiState: StateFlow<Joke?>
        get() = _uiState

    init {
        fetchNewJoke()
    }

    fun fetchNewJoke() {
        viewModelScope.launch {
            _uiState.value = jokeRepository.getJoke()
        }
    }

}