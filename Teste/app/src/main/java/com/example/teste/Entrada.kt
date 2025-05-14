package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "entrada",
    foreignKeys = [
        ForeignKey(
            entity = Funcionario::class,
            parentColumns = ["registro"],
            childColumns = ["funcionario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Entrada(
    @PrimaryKey(autoGenerate = true) val nentrada: Int,
    val data: String,
    val hora: String,
    val funcionario: Int
)
