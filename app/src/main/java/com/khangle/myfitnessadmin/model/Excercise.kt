package com.khangle.myfitnessadmin.model

import android.os.Parcel
import android.os.Parcelable

class Excercise(
    val id: String = "",
    var name: String,
    var difficulty: String,
    var equipment: String,
    var tutorial: String,
    val picSteps: List<String>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(difficulty)
        parcel.writeString(equipment)
        parcel.writeString(tutorial)
        parcel.writeStringList(picSteps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Excercise> {
        override fun createFromParcel(parcel: Parcel): Excercise {
            return Excercise(parcel)
        }

        override fun newArray(size: Int): Array<Excercise?> {
            return arrayOfNulls(size)
        }
    }
}
