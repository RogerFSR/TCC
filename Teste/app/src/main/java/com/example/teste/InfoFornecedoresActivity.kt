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
import com.example.teste.adapters.FornecedorAdapter
import com.example.teste.database.AppDatabase
import com.example.teste.database.Fornecedor
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoFornecedoresActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FornecedorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.info_fornecedores)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnVoltarFornecedores)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupButtons()
        carregarFornecedores()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFornecedores)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FornecedorAdapter(emptyList(),
            onEditarClickListener = { fornecedor ->
                abrirEdicaoFornecedor(fornecedor)
            },
            onExcluirClickListener = { fornecedor ->
                confirmarExclusao(fornecedor)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.list_to_main).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnNovoFornecedor).setOnClickListener {
            startActivity(Intent(this, NovoFornecedorActivity::class.java))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun carregarFornecedores() {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoFornecedoresActivity)
            val fornecedores = db.fornecedorDAO().getAllFornecedores()

            launch(Dispatchers.Main) {
                adapter = FornecedorAdapter(fornecedores,
                    onEditarClickListener = { fornecedor ->
                        abrirEdicaoFornecedor(fornecedor)
                    },
                    onExcluirClickListener = { fornecedor ->
                        confirmarExclusao(fornecedor)
                    }
                )
                recyclerView.adapter = adapter
            }
        }
    }

    private fun abrirEdicaoFornecedor(fornecedor: Fornecedor) {
        val intent = Intent(this, EditarFornecedorActivity::class.java).apply {
            putExtra("ID_FORNECEDOR", fornecedor.cnpj_cpf)
        }
        startActivity(intent)
    }

    private fun confirmarExclusao(fornecedor: Fornecedor) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Deseja realmente excluir o fornecedor ${fornecedor.nome}?")
            .setPositiveButton("Excluir") { _, _ ->
                excluirFornecedor(fornecedor)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun excluirFornecedor(fornecedor: Fornecedor) {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoFornecedoresActivity)
            db.fornecedorDAO().delete(fornecedor)
            carregarFornecedores()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarFornecedores()
    }
}
