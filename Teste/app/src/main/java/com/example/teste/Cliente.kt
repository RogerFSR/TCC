package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "cliente", primaryKeys = ["cpf"])
data class Cliente(
    @PrimaryKey val cpf: String,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "end") val endereco: String,
    @ColumnInfo(name = "bairro") val bairro: String,
    @ColumnInfo(name = "cep") val cep: String,
    @ColumnInfo(name = "cidade") val cidade: String,
    @ColumnInfo(name = "telefone") val telefone: String,
    @ColumnInfo(name = "telefone2") val telefone2: String,
    @ColumnInfo(name = "email") val email: String
)
