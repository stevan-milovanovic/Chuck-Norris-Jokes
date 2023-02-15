package rs.smobile.chucknorrisjokes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import rs.smobile.chucknorrisjokes.data.api.model.Joke
import rs.smobile.chucknorrisjokes.ui.theme.ChuckNorrisJokesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel(modelClass = MainViewModel::class.java)
            val uiState by viewModel.uiState.collectAsState()

            ChuckNorrisJokesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JokeGeneratorSection(
                        uiState,
                        viewModel::fetchNewJoke
                    )
                }
            }
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
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        GenerateJokeButton(onGenerateJokeButtonClick)
        when (uiState) {
            is MainUiState.Failure -> JokeCard(text = uiState.message)
            MainUiState.Loading -> LinearProgressIndicator(
                modifier = Modifier.padding(top = 20.dp)
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
            text = "Generate new joke",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun JokeCard(
    text: String?
) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth(),
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

@Preview(showBackground = true, device = PIXEL_4, showSystemUi = true)
@Composable
fun SuccessPreview() {
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

@Preview(showBackground = true, device = PIXEL_4, showSystemUi = true)
@Composable
fun FailurePreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            MainUiState.Failure("Joke couldn't be fetched.")
        ) {}
    }
}

@Preview(showBackground = true, device = PIXEL_4, showSystemUi = true)
@Composable
fun LoadingPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            MainUiState.Loading
        ) {}
    }
}