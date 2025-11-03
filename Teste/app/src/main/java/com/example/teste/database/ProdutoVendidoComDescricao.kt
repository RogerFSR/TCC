package com.example.teste.database

data class ProdutoVendidoComDescricao(
    val nVenda: Int,
    val codigobarras: String,
    val descricao: String,
    val qtd: Int,
    val valorvenda: Double,
    val subtotal: Double
)
