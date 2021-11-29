package com.khangle.myfitnessadmin.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BodyStat(val id: String, var name: String, var dataType: String, var unit: String): Parcelable
