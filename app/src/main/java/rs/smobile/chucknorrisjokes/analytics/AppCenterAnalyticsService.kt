package rs.smobile.chucknorrisjokes.analytics

import com.microsoft.appcenter.analytics.Analytics

class AppCenterAnalyticsService : AnalyticsService {

    override fun logEvent(name: String) {
        Analytics.trackEvent(name)
    }

    override fun logEvent(name: String, params: HashMap<String, String>) {
        Analytics.trackEvent(name, params)
    }

}