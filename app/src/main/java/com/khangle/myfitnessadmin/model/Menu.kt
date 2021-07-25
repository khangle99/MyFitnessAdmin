package com.khangle.myfitnessadmin.model

data class Menu(
    val id: String,
    var breakfast: String,
    var lunch: String,
    var dinner: String,
    var snack: String,
    var other: String,
    val picUrls: List<String>
)
