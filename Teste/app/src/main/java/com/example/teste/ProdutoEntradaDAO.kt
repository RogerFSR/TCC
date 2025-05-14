package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface ProdutoEntradaDAO {

    @Insert
    suspend fun insert(produtoEntrada: ProdutoEntrada)

    @Update
    suspend fun update(produtoEntrada: ProdutoEntrada)

    @Delete
    suspend fun delete(produtoEntrada: ProdutoEntrada)

    @Query("SELECT * FROM produtos_entrada WHERE nentrada = :nentrada AND codigobarras = :codigobarras")
    suspend fun getProdutoEntrada(nentrada: Int, codigobarras: String): ProdutoEntrada?

    @Query("SELECT * FROM produtos_entrada")
    suspend fun getAllProdutosEntrada(): List<ProdutoEntrada>
}
