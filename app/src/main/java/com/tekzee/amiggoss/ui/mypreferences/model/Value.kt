package com.tekzee.amiggoss.ui.mypreferences.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Value(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("is_set")
    var isSet: String = "",
    @SerializedName("iso2_code")
    val iso2Code: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("min")
    val min: String = "",
    @SerializedName("max")
    val max: String = "",
    @SerializedName("filter_from")
    var filter_from: String = "",
    @SerializedName("filter_to")
    var filter_to: String = ""


    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(isSet)
        parcel.writeString(iso2Code)
        parcel.writeString(name)
        parcel.writeString(min)
        parcel.writeString(max)
        parcel.writeString(filter_from)
        parcel.writeString(filter_to)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Value> {
        override fun createFromParcel(parcel: Parcel): Value {
            return Value(parcel)
        }

        override fun newArray(size: Int): Array<Value?> {
            return arrayOfNulls(size)
        }
    }
}