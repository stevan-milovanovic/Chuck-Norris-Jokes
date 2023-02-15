package rs.smobile.chucknorrisjokes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rs.smobile.chucknorrisjokes.ui.theme.ChuckNorrisJokesTheme

class MainActivity : ComponentActivity() {

    private var currentJoke: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChuckNorrisJokesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JokeGeneratorSection(currentJoke)
                }
            }
        }
    }
}

@Composable
private fun JokeGeneratorSection(
    joke: String? = null
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenerateJokeButton {}
        Joke(text = joke)
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
fun Joke(text: String?) {
    Text(
        text = text ?: "",
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true, device = PIXEL_4, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ChuckNorrisJokesTheme {
        JokeGeneratorSection("Chuck Norris makes great charcoal grilled cannibal kabobs.")
    }
}