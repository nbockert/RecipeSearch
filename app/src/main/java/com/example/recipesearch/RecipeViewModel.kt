package com.example.recipesearch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearch.model.Meal
import com.example.recipesearch.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RecipeUiState {
    object Idle : RecipeUiState()
    object Loading : RecipeUiState()
    data class Success(val recipes: List<Meal>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Idle)
    val uiState: StateFlow<RecipeUiState> = _uiState

    fun search(query: String) {

        viewModelScope.launch {
            _uiState.value = RecipeUiState.Loading
            try {
                val response = ApiClient.apiService.searchRecipes(
                    query,
                )
                _uiState.value = RecipeUiState.Success(response.meals ?: emptyList())
            } catch (e: Exception) {
                _uiState.value = RecipeUiState.Error("Failed: ${e.localizedMessage}")
            }
        }
    }


}