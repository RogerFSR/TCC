package com.example.teste.database
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
        )
    ]
)
data class ProdutoVendido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // <- PK do item
    val nVenda: Int,                                  // <- FK da venda
    val codigobarras: String,
    var qtd: Int,
    val valorvenda: Double,
    var subtotal: Double
)
