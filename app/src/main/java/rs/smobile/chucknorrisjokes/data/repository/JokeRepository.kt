package rs.smobile.chucknorrisjokes.data.repository

import rs.smobile.chucknorrisjokes.data.api.ApiConstants.GENERAL_SERVER_ERROR
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.TOO_MANY_REQUESTS_ERROR_MESSAGE
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.TOO_MANY_REQUESTS_HTTP_RESPONSE_STATUS_CODE
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.UNAUTHORIZED_ERROR_MESSAGE
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.UNAUTHORIZED_HTTP_RESPONSE_STATUS_CODE
import rs.smobile.chucknorrisjokes.data.api.JokeApi
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import javax.inject.Inject

class JokeRepository @Inject constructor(
    private val jokeApi: JokeApi
) {
    suspend fun getJoke(): Resource<Joke> {
        return try {
            val response = jokeApi.getJoke()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                when (response.code()) {
                    UNAUTHORIZED_HTTP_RESPONSE_STATUS_CODE -> Resource.Error(
                        UNAUTHORIZED_ERROR_MESSAGE
                    )
                    TOO_MANY_REQUESTS_HTTP_RESPONSE_STATUS_CODE -> Resource.Error(
                        TOO_MANY_REQUESTS_ERROR_MESSAGE
                    )
                    else -> if (response.message().isNullOrEmpty()) {
                        Resource.Error(GENERAL_SERVER_ERROR)
                    } else {
                        Resource.Error(response.message())
                    }
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: GENERAL_SERVER_ERROR)
        }
    }

}