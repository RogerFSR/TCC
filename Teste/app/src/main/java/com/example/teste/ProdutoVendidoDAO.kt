package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface ProdutoVendidoDAO {

    @Insert
    suspend fun insert(produtoVendido: ProdutoVendido)

    @Update
    suspend fun update(produtoVendido: ProdutoVendido)

    @Delete
    suspend fun delete(produtoVendido: ProdutoVendido)

    @Query("SELECT * FROM produtos_vendidos WHERE nVenda = :nVenda AND codigobarras = :codigobarras")
    suspend fun getProdutoVendido(nVenda: Int, codigobarras: String): ProdutoVendido?

    @Query("SELECT * FROM produtos_vendidos")
    suspend fun getAllProdutosVendidos(): List<ProdutoVendido>
}
