package rs.smobile.chucknorrisjokes.analytics

interface AnalyticsService {

    fun logEvent(name: String)

    fun logEvent(name: String, params: HashMap<String, String>)

}