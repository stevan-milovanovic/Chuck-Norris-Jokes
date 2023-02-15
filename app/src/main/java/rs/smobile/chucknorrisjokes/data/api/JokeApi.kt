package rs.smobile.chucknorrisjokes.data.api

import retrofit2.http.GET
import rs.smobile.chucknorrisjokes.data.api.model.Joke

interface JokeApi {

    @GET(ApiConstants.JOKE_ENDPOINT)
    suspend fun getJoke(): Joke

}