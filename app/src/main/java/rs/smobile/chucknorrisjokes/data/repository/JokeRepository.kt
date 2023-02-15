package rs.smobile.chucknorrisjokes.data.repository

import rs.smobile.chucknorrisjokes.data.api.JokeApi
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import javax.inject.Inject

class JokeRepository @Inject constructor(
    private val jokeApi: JokeApi
) {

    suspend fun getJoke(): Joke = jokeApi.getJoke()

}