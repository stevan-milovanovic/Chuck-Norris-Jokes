package rs.smobile.chucknorrisjokes.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import rs.smobile.chucknorrisjokes.data.api.model.Joke

interface JokeApi {

    @GET(ApiConstants.JOKE_ENDPOINT)
    suspend fun getJoke(
        @Query(ApiConstants.CATEGORY_QUERY_PARAM_NAME) category: String?
    ): Response<Joke>

    @GET(ApiConstants.JOKE_CATEGORY_ENDPOINT)
    suspend fun getJokeCategories(): Response<List<String>>

}