package com.example.recipesapp.ui.fragment.home

data class FoodModel(
    var name:String?="",
    var recipe:List<String>?= mutableListOf(),
    var imgUrl:String?=""
    )