package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "produtos_entrada",
    foreignKeys = [
        ForeignKey(
            entity = Entrada::class,
            parentColumns = ["nentrada"],
            childColumns = ["nentrada"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Produto::class,
            parentColumns = ["codigobarras"],
            childColumns = ["codigobarras"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProdutoEntrada(
    @PrimaryKey val nentrada: Int,
    val codigobarras: String,
    val qtd: Int,
    val valorcompra: Double,
    val subtotal: Double,
    val lote: String,
    val validade: String
)
