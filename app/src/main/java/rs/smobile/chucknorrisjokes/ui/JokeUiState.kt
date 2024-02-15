package rs.smobile.chucknorrisjokes.ui

import rs.smobile.chucknorrisjokes.data.api.model.Joke

sealed class JokeUiState(
    open val categories: List<String>,
    open val selectedCategory: String?
) {
    data class Success(
        val joke: Joke?,
        override val categories: List<String>,
        override val selectedCategory: String?
    ) : JokeUiState(categories, selectedCategory)

    data class Failure(
        override val categories: List<String>,
        override val selectedCategory: String?,
        val message: String?
    ) : JokeUiState(categories, selectedCategory)

    data class Loading(
        override val categories: List<String>,
        override val selectedCategory: String?
    ) : JokeUiState(categories, selectedCategory)

    fun copyWithSelectedCategory(selectedCategory: String?): JokeUiState = when (this) {
        is Failure -> copy(selectedCategory = selectedCategory)
        is Loading -> copy(selectedCategory = selectedCategory)
        is Success -> copy(selectedCategory = selectedCategory)
    }
}