package com.example.recipesearch
import com.example.recipesearch.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    @GET("search")
    suspend fun searchRecipes(
        @Query("type") type: String = "public",
        @Query("q") query: String,
        @Query("app_id") appId: String = BuildConfig.EDAMAM_API_ID,
        @Query("app_key") appKey: String = BuildConfig.EDAMAM_API_KEY
    ): RecipeResponse
}
