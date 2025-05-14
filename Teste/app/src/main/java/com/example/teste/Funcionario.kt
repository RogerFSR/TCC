package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "funcionario")
data class Funcionario(
    @PrimaryKey(autoGenerate = true) val registro: Int,
    val nome: String,
    val telefone: String
)
