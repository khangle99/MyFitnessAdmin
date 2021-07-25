package com.khangle.myfitnessadmin.model

class Excercise(
    val id: String,
    var name: String,
    var difficulty: String,
    var equipment: String,
    var tutorial: String,
    val picSteps: List<String>
)
