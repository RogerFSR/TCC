package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface FuncionarioDAO {

    @Insert
    suspend fun insert(funcionario: Funcionario)

    @Update
    suspend fun update(funcionario: Funcionario)

    @Delete
    suspend fun delete(funcionario: Funcionario)

    @Query("SELECT * FROM funcionario WHERE registro = :registro")
    suspend fun getFuncionarioByRegistro(registro: Int): Funcionario?

    @Query("SELECT * FROM funcionario")
    suspend fun getAllFuncionarios(): List<Funcionario>
}
