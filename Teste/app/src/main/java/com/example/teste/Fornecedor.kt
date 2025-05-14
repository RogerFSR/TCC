package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fornecedor")
data class Fornecedor(
    @PrimaryKey val cnpj_cpf: String,
    val nome: String,
    val end: String,
    val bairro: String,
    val cep: String,
    val cidade: String,
    val estado: String,
    val telefone: String,
    val email: String
)
