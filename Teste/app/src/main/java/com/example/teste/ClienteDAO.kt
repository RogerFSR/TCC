package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface ClienteDAO {

    @Insert
    suspend fun insert(cliente: Cliente)

    @Update
    suspend fun update(cliente: Cliente)

    @Delete
    suspend fun delete(cliente: Cliente)

    @Query("SELECT * FROM cliente WHERE cpf = :cpf")
    suspend fun getClienteByCpf(cpf: String): Cliente?

    @Query("SELECT * FROM cliente")
    suspend fun getAllClientes(): List<Cliente>
}
