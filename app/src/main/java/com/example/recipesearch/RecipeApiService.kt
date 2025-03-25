package com.example.recipesearch
import com.example.recipesearch.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    @GET("search.php")
    suspend fun searchRecipes(
        @Query("s") query: String,
    ): MealResponse
}
