package com.example.recipesapp.ui.fragment.home

import android.os.Parcel
import android.os.Parcelable


data class FoodModel(
    var name: String? = "",
    var ingredients: List<String>? = mutableListOf(),
    var imgUrl: String? = "",
    var recipe: List<String>? = mutableListOf()
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(ingredients)
        parcel.writeString(imgUrl)
        parcel.writeStringList(recipe)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodModel> {
        override fun createFromParcel(parcel: Parcel): FoodModel {
            return FoodModel(parcel)
        }

        override fun newArray(size: Int): Array<FoodModel?> {
            return arrayOfNulls(size)
        }
    }
}