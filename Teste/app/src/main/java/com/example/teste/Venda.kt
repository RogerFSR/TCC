package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "vendas",
    foreignKeys = [
        ForeignKey(
            entity = Funcionario::class,
            parentColumns = ["registro"],
            childColumns = ["funcionario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["cpf"],
            childColumns = ["cliente"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Venda(
    @PrimaryKey(autoGenerate = true) val nVenda: Int,
    val data: String,
    val hora: String,
    val funcionario: Int,
    val cliente: String,
    val pagou: Boolean
)
