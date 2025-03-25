package com.example.recipesearch
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import com.example.recipesearch.model.Meal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { RecipeViewModel() }
            val state by viewModel.uiState.collectAsState()

            var query by remember { mutableStateOf("") }


            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MealFinder",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search Recipe") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.search(query) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Search")
                }

                Spacer(Modifier.height(16.dp))

                when (state) {
                    is RecipeUiState.Idle -> Text("Enter a search term")
                    is RecipeUiState.Loading -> CircularProgressIndicator()
                    is RecipeUiState.Error -> Text((state as RecipeUiState.Error).message)
                    is RecipeUiState.Success -> RecipeList((state as RecipeUiState.Success).recipes)
                }
            }
        }
    }
}

@Composable
fun RecipeList(recipes: List<Meal>) {
    LazyColumn {
        items(recipes) { recipe ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expanded=!expanded },

                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(recipe.strMeal, style = MaterialTheme.typography.titleMedium)
                    Text("Category: ${recipe.strCategory}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))

                    if (recipe.strYoutube.isNotBlank()) {
                        YouTubePlayer(recipe.strYoutube)
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    AnimatedVisibility(visible = expanded) {
                        Text(
                            recipe.strInstructions,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (!expanded) {
                        Text("Tap to expand", style = MaterialTheme.typography.labelSmall)
                    }
                }

                }
            }
        }
    }

@Composable
fun YouTubePlayer(videoUrl: String) {
    val context = LocalContext.current
    val videoId = videoUrl.substringAfter("watch?v=")
    val embedUrl = "https://www.youtube.com/embed/$videoId"
    val html = """
        <html>
        <body style="margin:0;padding:0;">
            <iframe width="100%" height="100%" src="$embedUrl" frameborder="0" allowfullscreen></iframe>
        </body>
        </html>
    """.trimIndent()
    AndroidView(
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

