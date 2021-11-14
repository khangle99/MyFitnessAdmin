package com.khangle.myfitnessadmin.model

import android.os.Parcel
import android.os.Parcelable

class BodyStat(val id: String, var name: String, var dataType: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(dataType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyStat> {
        override fun createFromParcel(parcel: Parcel): BodyStat {
            return BodyStat(parcel)
        }

        override fun newArray(size: Int): Array<BodyStat?> {
            return arrayOfNulls(size)
        }
    }

}
