package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.adapters.TipoAdapter
import com.example.teste.database.AppDatabase
import com.example.teste.database.Tipo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoTiposActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TipoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.info_tipos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnVoltarTipos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupButtons()
        carregarTipos()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewTipos)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btnVoltarTipos).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnNovoTipo).setOnClickListener {
            startActivity(Intent(this, NovoTipoActivity::class.java))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun carregarTipos() {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoTiposActivity)
            val tipos = db.tipoDAO().getAllTipos()

            launch(Dispatchers.Main) {
                adapter = TipoAdapter(tipos, this@InfoTiposActivity) { tipo ->
                    confirmarExclusao(tipo)
                }
                recyclerView.adapter = adapter
            }
        }
    }

    private fun confirmarExclusao(tipo: Tipo) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Deseja realmente excluir o tipo ${tipo.descricao}?")
            .setPositiveButton("Excluir") { _, _ -> excluirTipo(tipo) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun excluirTipo(tipo: Tipo) {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoTiposActivity)
            db.tipoDAO().delete(tipo)
            carregarTipos()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarTipos()
    }
}
