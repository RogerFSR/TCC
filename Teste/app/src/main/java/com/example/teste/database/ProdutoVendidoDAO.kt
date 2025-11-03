package com.example.teste.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface ProdutoVendidoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(produtoVendido: ProdutoVendido)

    @Update
    suspend fun update(produtoVendido: ProdutoVendido)

    @Delete
    suspend fun delete(produtoVendido: ProdutoVendido)

    @Query("SELECT * FROM produtos_vendidos WHERE nVenda = :nVenda AND codigobarras = :codigobarras")
    suspend fun getProdutoVendido(nVenda: Int, codigobarras: String): ProdutoVendido?

    @Query("SELECT * FROM produtos_vendidos")
    suspend fun getAllProdutosVendidos(): List<ProdutoVendido>

    @Query("SELECT * FROM produtos_vendidos WHERE nVenda = :vendaId")
    suspend fun getItensByVenda(vendaId: Int): List<ProdutoVendido>

    @Query("""
        SELECT pv.nVenda, pv.codigobarras, p.nome AS descricao, pv.qtd, pv.valorvenda, pv.subtotal
        FROM produtos_vendidos pv
        JOIN produtos p ON pv.codigobarras = p.codigobarras
        WHERE pv.nVenda = :nVenda
    """)
    suspend fun getItensComDescricaoByVenda(nVenda: Int): List<ProdutoVendidoComDescricao>
}
