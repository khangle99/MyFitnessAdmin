package com.khangle.myfitnessadmin.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.HashMap
@Parcelize
class Excercise(
    val id: String = "",
    var name: String,
    var difficulty: Int,
    var equipment: String,
    var caloFactor: Float,
    var tutorial: List<String>,
    var levelJSON: Map<String, Array<Int>>,
    val picSteps: List<String>,
    var achieveEnsure: String, // json
    val addedCount: Int,
    var tutorialWithPic: Map<String, String> = mapOf(),
    var categoryId: String,
    var nutriFactor: String
): Parcelable
