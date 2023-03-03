package com.example.cryptoapp.presentation.item

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CryptoItem(
    val id: String?,
    val abbr: String?,
    val title: String?,
    val imageLink: String?,
    val price: String?,
    var marketCap: String?,
    var priceChange: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CryptoItem> {
        override fun createFromParcel(parcel: Parcel): CryptoItem {
            return CryptoItem(parcel)
        }

        override fun newArray(size: Int): Array<CryptoItem?> {
            return arrayOfNulls(size)
        }
    }
}
