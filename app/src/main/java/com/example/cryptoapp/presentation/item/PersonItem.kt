package com.example.cryptoapp.presentation.item

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptoapp.data.entities.PersonEntity

data class PersonItem (
    var name: String?,
    var lastName: String?,
    var date: String?,
    var fileName: String?,
    var URI: String?
)

fun PersonItem.toPersonEntity(): PersonEntity {
    return PersonEntity(
        name = name,
        lastName = lastName,
        date = date,
        fileName = fileName,
        URI = URI
    )
}
