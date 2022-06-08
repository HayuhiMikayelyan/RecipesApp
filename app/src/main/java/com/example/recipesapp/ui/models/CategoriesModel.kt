package com.example.recipesapp.ui.models

import android.os.Parcel
import android.os.Parcelable

data class CategoriesModel(
    var background: String? = "",
    var imgUrl: String? = "",
    var name: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(background)
        parcel.writeString(imgUrl)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoriesModel> {
        override fun createFromParcel(parcel: Parcel): CategoriesModel {
            return CategoriesModel(parcel)
        }

        override fun newArray(size: Int): Array<CategoriesModel?> {
            return arrayOfNulls(size)
        }
    }
}