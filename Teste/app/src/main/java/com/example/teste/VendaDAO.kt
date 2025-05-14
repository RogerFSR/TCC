package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface VendaDAO {

    @Insert
    suspend fun insert(venda: Venda)

    @Update
    suspend fun update(venda: Venda)

    @Delete
    suspend fun delete(venda: Venda)

    @Query("SELECT * FROM vendas WHERE nVenda = :nVenda")
    suspend fun getVendaById(nVenda: Int): Venda?

    @Query("SELECT * FROM vendas")
    suspend fun getAllVendas(): List<Venda>
}
