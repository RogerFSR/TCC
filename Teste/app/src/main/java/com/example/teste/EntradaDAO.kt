package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface EntradaDAO {

    @Insert
    suspend fun insert(entrada: Entrada)

    @Update
    suspend fun update(entrada: Entrada)

    @Delete
    suspend fun delete(entrada: Entrada)

    @Query("SELECT * FROM entrada WHERE nentrada = :nentrada")
    suspend fun getEntradaById(nentrada: Int): Entrada?

    @Query("SELECT * FROM entrada")
    suspend fun getAllEntradas(): List<Entrada>
}
