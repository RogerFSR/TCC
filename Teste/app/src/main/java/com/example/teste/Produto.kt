package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "produtos",
    foreignKeys = [
        ForeignKey(
            entity = Tipo::class,
            parentColumns = ["codtipo"],
            childColumns = ["tipo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Fornecedor::class,
            parentColumns = ["cnpj_cpf"],
            childColumns = ["fornecedor"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Produto(
    @PrimaryKey val codigobarras: String,
    val nome: String,
    val tipo: Int,
    val geladeira: Boolean,
    val fornecedor: String,
    val estoque_min: Int,
    val estoque_atual: Int,
    val estoque_max: Int
)
