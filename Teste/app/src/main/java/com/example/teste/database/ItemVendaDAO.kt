package com.example.teste.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ItemVendaDAO {

    @Query("SELECT * FROM itens_venda WHERE vendaId = :vendaId")
    suspend fun getItensByVenda(vendaId: Int): List<ItemVenda>
}
