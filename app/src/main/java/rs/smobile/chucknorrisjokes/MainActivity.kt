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
            val joke by viewModel.uiState.collectAsState()

            ChuckNorrisJokesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JokeGeneratorSection(
                        joke,
                        viewModel::fetchNewJoke
                    )
                }
            }
        }
    }
}

@Composable
private fun JokeGeneratorSection(
    joke: Joke?,
    onGenerateJokeButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenerateJokeButton(onGenerateJokeButtonClick)
        JokeCard(text = joke?.value)
    }
}

@Composable
private fun GenerateJokeButton(
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick
    ) {
        Text(text = "Generate new joke")
    }
}

@Composable
fun JokeCard(text: String?) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text ?: "",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, device = PIXEL_4, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection(
            Joke(
                categories = emptyList(),
                createdAt = "",
                iconUrl = "",
                id = "",
                updatedAt = "",
                url = "",
                value = "Q: Which is heavier, a ton of bricks or a ton of feathers? A: Chuck Norris."
            )
        ) {}
    }
}