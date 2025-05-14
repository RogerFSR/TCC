package com.example.teste

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Cliente::class, Funcionario::class, Entrada::class, Fornecedor::class, Tipo::class,
        Produto::class, ProdutoEntrada::class, Venda::class, ProdutoVendido::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clienteDAO(): ClienteDAO
    abstract fun funcionarioDAO(): FuncionarioDAO
    abstract fun entradaDAO(): EntradaDAO
    abstract fun fornecedorDAO(): FornecedorDAO
    abstract fun tipoDAO(): TipoDAO
    abstract fun produtoDAO(): ProdutoDAO
    abstract fun produtoEntradaDAO(): ProdutoEntradaDAO
    abstract fun vendaDAO(): VendaDAO
    abstract fun produtoVendidoDAO(): ProdutoVendidoDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "controle_estoque.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
