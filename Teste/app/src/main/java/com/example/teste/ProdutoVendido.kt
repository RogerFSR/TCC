package com.example.teste

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "produtos_vendidos",
    foreignKeys = [
        ForeignKey(
            entity = Venda::class,
            parentColumns = ["nVenda"],
            childColumns = ["nVenda"],
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
data class ProdutoVendido(
    @PrimaryKey val nVenda: Int,
    val codigobarras: String,
    val qtd: Int,
    val valorvenda: Double,
    val subtotal: Double
)
