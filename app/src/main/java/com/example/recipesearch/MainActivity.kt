package com.example.recipesearch
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.RecipeUiState
import com.example.recipesearch.RecipeViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { RecipeViewModel() }
            val state by viewModel.uiState.collectAsState()

            var query by remember { mutableStateOf("") }

            Column(Modifier.fillMaxSize().padding(16.dp)) {
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
fun RecipeList(recipes: List<Recipe>) {
    LazyColumn {
        items(recipes) { recipe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(recipe.image),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(recipe.label, style = MaterialTheme.typography.titleMedium)
                        Text(recipe.source, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
