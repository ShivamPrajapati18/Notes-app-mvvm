package com.example.notesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    val title:String,
    val description:String,
    val timestamp:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
