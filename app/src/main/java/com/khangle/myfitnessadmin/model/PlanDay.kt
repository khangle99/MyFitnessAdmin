package com.khangle.myfitnessadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlanDay(val id: String, val categoryId: String, val excId: String, var day: String, var exc: Excercise? = null): Parcelable