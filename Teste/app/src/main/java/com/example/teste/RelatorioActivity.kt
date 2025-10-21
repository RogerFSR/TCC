package com.example.teste

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.adapters.VendaAdapter
import com.example.teste.database.AppDatabase
import kotlinx.coroutines.launch

class RelatorioActivity : AppCompatActivity() {

    private lateinit var recyclerVendas: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relatorio_activity)

        recyclerVendas = findViewById(R.id.recyclerVendas)
        recyclerVendas.layoutManager = LinearLayoutManager(this)


        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@RelatorioActivity)
            val listaVendas = db.vendaDAO().getAllVendas()
//            recyclerVendas.adapter = VendaAdapter(listaDeItens)
        }

    }

    // private fun printVenda(venda: Venda) {
    //     // Implementar lógica de impressão aqui futuramente
    // }
}
