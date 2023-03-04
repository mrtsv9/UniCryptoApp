package com.example.cryptoapp.data.entities

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptoapp.presentation.item.PersonItem

@Entity(tableName = "person_table")
data class PersonEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val name: String?,
    val lastName: String?,
    val date: String?,
    val fileName: String?,
    val URI: String?
)

fun PersonEntity.toPersonItem(): PersonItem {
    return PersonItem(
        name = name,
        lastName = lastName,
        date = date,
        fileName = fileName,
        URI = URI
    )
}
