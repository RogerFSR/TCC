package com.example.teste.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "itens_venda",
    foreignKeys = [ForeignKey(
        entity = Venda::class,
        parentColumns = ["nVenda"],
        childColumns = ["vendaId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ItemVenda(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vendaId: Int,              // referência à venda
    val codigo: String,
    val descricao: String,
    val quantidade: Int,
    val valorUnitario: Double
) {
    val valorTotal: Double
        get() = quantidade * valorUnitario
}
