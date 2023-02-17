package rs.smobile.chucknorrisjokes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.microsoft.appcenter.espresso.Factory
import com.microsoft.appcenter.espresso.ReportHelper
import org.junit.After
import org.junit.Rule
import org.junit.Test
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import rs.smobile.chucknorrisjokes.viewmodel.MainUiState

class MainScreenComposableTest {

    @Rule
    @JvmField
    var composeTestRule = createComposeRule()

    @Rule
    @JvmField
    var reportHelper: ReportHelper = Factory.getReportHelper()

    @After
    fun tearDown() {
        reportHelper.label("Test Screenshot")
    }

    @Test
    fun checkLoadingStateUi() {
        setMainScreenComposableContent(MainUiState.Loading)
        verifyProgressIndicatorIsDisplayed()
    }

    @Test
    fun checkFailureStateUi() {
        setMainScreenComposableContent(MainUiState.Failure("test error"))
        verifyProgressIndicatorDoesNotExist()
        verifyJokeCardIsDisplayed()
    }

    @Test
    fun checkSuccessStateUi() {
        setMainScreenComposableContent(
            MainUiState.Success(
                Joke(
                    categories = emptyList(), createdAt = "date",
                    iconUrl = "iconUrl", id = "id", updatedAt = "updatedAt",
                    url = "url", value = "Joke test value"
                )
            )
        )

        verifyProgressIndicatorDoesNotExist()
        verifyJokeCardIsDisplayed()
    }

    private fun setMainScreenComposableContent(uiState: MainUiState) {
        composeTestRule.setContent { MainScreenComposable(uiState) {} }
    }

    private fun verifyJokeCardIsDisplayed() {
        composeTestRule
            .onNodeWithTag(JOKE_CARD_TEST_TAG)
            .assertIsDisplayed()
    }

    private fun verifyProgressIndicatorDoesNotExist() {
        composeTestRule
            .onNodeWithTag(PROGRESS_INDICATOR_TEST_TAG)
            .assertDoesNotExist()
    }

    private fun verifyProgressIndicatorIsDisplayed() {
        composeTestRule
            .onNodeWithTag(PROGRESS_INDICATOR_TEST_TAG)
            .assertExists("Loading indicator is not in the view hierarchy")
            .assertIsDisplayed()
    }

}