package rs.smobile.chucknorrisjokes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    fetchNewJoke: () -> Unit,
    onJokeCategorySelected: (String) -> Unit
) {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            uiState,
            fetchNewJoke,
            onJokeCategorySelected
        )
    }
}

@Composable
private fun JokeGeneratorSection(
    uiState: JokeUiState,
    onGenerateJokeButtonClick: () -> Unit,
    onJokeCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.liquid_cheese),
                contentScale = ContentScale.FillBounds,
                alpha = 0.4f
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenerateJokeButton(onGenerateJokeButtonClick)
        when (uiState) {
            is JokeUiState.Loading -> LinearProgressIndicator(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .testTag(PROGRESS_INDICATOR_TEST_TAG)
            )

            else -> JokeCard(uiState)
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(uiState.categories) { category ->
                    JokeCategory(
                        category = category,
                        isSelected = uiState.selectedCategory != null && uiState.selectedCategory == category,
                        onJokeCategorySelected = onJokeCategorySelected
                    )
                }
            }
        )
    }
}

@Composable
private fun JokeCategory(
    category: String,
    isSelected: Boolean,
    onJokeCategorySelected: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onJokeCategorySelected(category) },
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = category,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.onSecondary else
                MaterialTheme.colorScheme.onTertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun GenerateJokeButton(
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 40.dp),
        shape = MaterialTheme.shapes.small,
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
        shape = MaterialTheme.shapes.small,
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
                joke = Joke(
                    categories = emptyList(),
                    createdAt = "",
                    iconUrl = "",
                    id = "",
                    updatedAt = "",
                    url = "",
                    value = "Q: Which is heavier, a ton of bricks or a ton of feathers? A: Chuck Norris."
                ),
                categories = listOf(),
                selectedCategory = null
            ),
            {}
        ) {}
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun FailurePreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            JokeUiState.Failure(
                categories = emptyList(),
                selectedCategory = null,
                message = "Joke couldn't be fetched."
            ), {}
        ) {}
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun LoadingPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            JokeUiState.Loading(categories = emptyList(), selectedCategory = null), {}
        ) {}
    }
}

@Preview
@Composable
private fun JokeCategoryPreview() {
    ChuckNorrisJokesTheme {
        JokeCategory(category = "general", isSelected = false) {}
    }
}
