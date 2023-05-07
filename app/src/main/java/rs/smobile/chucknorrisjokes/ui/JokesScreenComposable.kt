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
import rs.smobile.chucknorrisjokes.viewmodel.JokeUiState

const val PROGRESS_INDICATOR_TEST_TAG = "main_progress_indicator_test_tag"
const val JOKE_CARD_TEST_TAG = "joke_card_test_tag"

@Composable
fun JokesScreenComposable(
    uiState: JokeUiState,
    fetchNewJoke: () -> Unit
) {
    ChuckNorrisJokesTheme {
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
    uiState: JokeUiState,
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
            JokeUiState.Loading -> LinearProgressIndicator(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .testTag(PROGRESS_INDICATOR_TEST_TAG)
            )
            else -> JokeCard(uiState)
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
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.filledTonalButtonColors()
    ) {
        Text(
            text = stringResource(R.string.generate_new_joke),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun JokeCard(
    uiState: JokeUiState
) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .testTag(JOKE_CARD_TEST_TAG),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (uiState is JokeUiState.Success) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = when (uiState) {
                is JokeUiState.Success -> uiState.joke?.value.orEmpty()
                is JokeUiState.Failure -> uiState.message.orEmpty()
                else -> ""
            },
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun SuccessPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            JokeUiState.Success(
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
            JokeUiState.Failure("Joke couldn't be fetched.")
        ) {}
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun LoadingPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            JokeUiState.Loading
        ) {}
    }
}