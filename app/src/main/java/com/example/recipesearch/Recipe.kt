package com.example.recipesearch.model

data class MealResponse(
    val meals: List<Meal>?
)


data class Meal(
    val strMeal: String,
    val strCategory: String,
    val strInstructions: String,
    var strYoutube: String,
)
