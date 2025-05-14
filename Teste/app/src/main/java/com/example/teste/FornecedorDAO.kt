package com.example.teste

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface FornecedorDAO {

    @Insert
    suspend fun insert(fornecedor: Fornecedor)

    @Update
    suspend fun update(fornecedor: Fornecedor)

    @Delete
    suspend fun delete(fornecedor: Fornecedor)

    @Query("SELECT * FROM fornecedor WHERE cnpj_cpf = :cnpjCpf")
    suspend fun getFornecedorByCnpjCpf(cnpjCpf: String): Fornecedor?

    @Query("SELECT * FROM fornecedor")
    suspend fun getAllFornecedores(): List<Fornecedor>
}
