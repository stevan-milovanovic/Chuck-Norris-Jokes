package rs.smobile.chucknorrisjokes.data.api

object ApiConstants {
    const val BASE_URL = "https://matchilling-chuck-norris-jokes-v1.p.rapidapi.com/jokes/"
    const val JOKE_ENDPOINT = "random"

    const val HOST_HEADER_NAME = "X-RapidAPI-Host"
    const val HOST_HEADER_VALUE = "matchilling-chuck-norris-jokes-v1.p.rapidapi.com"

    const val API_KEY_HEADER_NAME = "X-RapidAPI-Key"
    const val API_KEY_HEADER_VALUE = "YOUR API KEY HERE"

    const val CONTENT_VALUE_HEADER_NAME = "accept"
    const val CONTENT_VALUE_HEADER_VALUE = "application/json"

    const val UNAUTHORIZED_HTTP_RESPONSE_STATUS_CODE = 401
    const val UNAUTHORIZED_ERROR_MESSAGE = "You're not authorized to see jokes about Chuck Norris."

    const val FORBIDDEN_HTTP_RESPONSE_STATUS_CODE = 403
    const val FORBIDDEN_ERROR_MESSAGE = "You are banned from accessing Chuck Norris jokes.\n\n" +
            "You should verify that you have set a valid API key."

    const val TOO_MANY_REQUESTS_HTTP_RESPONSE_STATUS_CODE = 429
    const val TOO_MANY_REQUESTS_ERROR_MESSAGE = "You're sending too many requests, let me breath."

    const val GENERAL_SERVER_ERROR = "An unexpected server error occurred."
}