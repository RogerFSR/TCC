package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tipos")
data class Tipo(
    @PrimaryKey(autoGenerate = true) val codtipo: Int,
    val descricao: String
)
