package rs.smobile.chucknorrisjokes.analytics

import rs.smobile.chucknorrisjokes.BuildConfig

object AnalyticsConstants {

    const val APP_CENTER_API_KEY = BuildConfig.APP_CENTER_API_KEY

    const val FETCH_INITIAL_JOKE = "fetch_initial_joke"
    const val FETCH_NEW_JOKE = "fetch_new_joke"

    const val NEW_JOKE_FETCHED = "new_joke_fetched"
    const val JOKE = "joke"
    const val JOKE_CREATED_AT = "joke_created_at"
    const val JOKE_CATEGORIES = "joke_categories"

    const val NEW_JOKE_FETCH_FAILED = "new_joke_fetch_failed"
    const val NEW_JOKE_FETCH_FAILURE_MESSAGE = "new_joke_fetch_failure_message"

}