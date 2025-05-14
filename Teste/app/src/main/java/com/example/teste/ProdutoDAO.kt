package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface ProdutoDAO {

    @Insert
    suspend fun insert(produto: Produto)

    @Update
    suspend fun update(produto: Produto)

    @Delete
    suspend fun delete(produto: Produto)

    @Query("SELECT * FROM produtos WHERE codigobarras = :codigobarras")
    suspend fun getProdutoByCodBarra(codigobarras: String): Produto?

    @Query("SELECT * FROM produtos")
    suspend fun getAllProdutos(): List<Produto>
}
