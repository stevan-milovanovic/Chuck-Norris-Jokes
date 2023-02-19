package rs.smobile.chucknorrisjokes.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rs.smobile.chucknorrisjokes.R
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import rs.smobile.chucknorrisjokes.ui.theme.ChuckNorrisJokesTheme
import rs.smobile.chucknorrisjokes.viewmodel.MainUiState

const val PROGRESS_INDICATOR_TEST_TAG = "main_progress_indicator_test_tag"
const val JOKE_CARD_TEST_TAG = "joke_card_test_tag"

@Composable
fun MainScreenComposable(
    uiState: MainUiState,
    fetchNewJoke: () -> Unit
) {
    ChuckNorrisJokesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            JokeGeneratorSection(
                uiState,
                fetchNewJoke
            )
        }
    }
}

@Composable
private fun JokeGeneratorSection(
    uiState: MainUiState,
    onGenerateJokeButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .paint(
                painterResource(id = R.drawable.liquid_cheese),
                contentScale = ContentScale.FillBounds,
                alpha = 0.4f
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenerateJokeButton(onGenerateJokeButtonClick)
        when (uiState) {
            is MainUiState.Failure -> JokeCard(text = uiState.message)
            MainUiState.Loading -> LinearProgressIndicator(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .testTag(PROGRESS_INDICATOR_TEST_TAG)
            )
            is MainUiState.Success -> JokeCard(text = uiState.joke?.value)
        }
    }
}

@Composable
private fun GenerateJokeButton(
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 40.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = stringResource(R.string.generate_new_joke),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun JokeCard(
    text: String?
) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .testTag(JOKE_CARD_TEST_TAG),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text ?: "",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun SuccessPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            MainUiState.Success(
                Joke(
                    categories = emptyList(),
                    createdAt = "",
                    iconUrl = "",
                    id = "",
                    updatedAt = "",
                    url = "",
                    value = "Q: Which is heavier, a ton of bricks or a ton of feathers? A: Chuck Norris."
                )
            )
        ) {}
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun FailurePreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            MainUiState.Failure("Joke couldn't be fetched.")
        ) {}
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun LoadingPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            MainUiState.Loading
        ) {}
    }
}