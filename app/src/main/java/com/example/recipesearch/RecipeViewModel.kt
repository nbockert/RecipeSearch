package com.example.recipesearch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RecipeUiState {
    object Idle : RecipeUiState()
    object Loading : RecipeUiState()
    data class Success(val recipes: List<Recipe>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Idle)
    val uiState: StateFlow<RecipeUiState> = _uiState

    // Access your secret API key injected via BuildConfig
    private val apiKey: String = BuildConfig.EDAMAM_API_KEY
    private val apiId: String = BuildConfig.EDAMAM_API_ID

    init {
        println("API Key: $apiKey")
    }

    fun search(query: String) {
        println("Using API Key: $apiKey")

        viewModelScope.launch {
            _uiState.value = RecipeUiState.Loading
            try {
                val response = ApiClient.apiService.searchRecipes(
                    query,
                    apiId,
                    apiKey
                )
                _uiState.value = RecipeUiState.Success(response.hits.map { it.recipe })
            } catch (e: Exception) {
                _uiState.value = RecipeUiState.Error("Failed: ${e.localizedMessage}")
            }
        }
    }

    // Additional function to print API key
    fun printApiKey() {
        println("API Key: $apiKey")
        // You could also log to Android's LogCat
        // Log.d("RecipeViewModel", "API Key: $apiKey")
    }
}