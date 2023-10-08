package rs.smobile.chucknorrisjokes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint
import rs.smobile.chucknorrisjokes.analytics.AnalyticsConstants.APP_CENTER_API_KEY
import rs.smobile.chucknorrisjokes.viewmodel.JokesViewModel

@AndroidEntryPoint
class JokesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCenter.start(application, APP_CENTER_API_KEY, Analytics::class.java, Crashes::class.java)

        setContent {
            val viewModel: JokesViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()
            JokesScreenComposable(
                uiState,
                viewModel::fetchNewJoke
            )
        }
    }

}
