package com.khangle.myfitnessadmin.common

enum class Difficulty(val raw: Int) {
    Beginner(0),
    Intermediate(1),
    Expert(2);

    companion object {
        fun fromInt(value: Int) = Difficulty.values().first { it.raw == value }
    }
}