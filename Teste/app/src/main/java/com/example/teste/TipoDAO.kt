package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface TipoDAO {

    @Insert
    suspend fun insert(tipo: Tipo)

    @Update
    suspend fun update(tipo: Tipo)

    @Delete
    suspend fun delete(tipo: Tipo)

    @Query("SELECT * FROM tipos WHERE codtipo = :codtipo")
    suspend fun getTipoById(codtipo: Int): Tipo?

    @Query("SELECT * FROM tipos")
    suspend fun getAllTipos(): List<Tipo>
}
