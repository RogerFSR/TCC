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
import com.example.teste.adapters.ClienteAdapter
import com.example.teste.database.AppDatabase
import com.example.teste.database.Cliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoClientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.info_clientes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnInfosToMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupButtons()
        carregarClientes()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ClienteAdapter(emptyList(),
            onEditarClickListener = { cliente ->
                abrirEdicaoCliente(cliente)
            },
            onExcluirClickListener = { cliente ->
                confirmarExclusao(cliente)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.list_to_main).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.novo_cliente).setOnClickListener {
            startActivity(Intent(this, NovoClienteActivity::class.java))
        }
    }

    private fun carregarClientes() {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoClientesActivity)
            val clientes = db.clienteDAO().getAllClientes()

            launch(Dispatchers.Main) {
                adapter = ClienteAdapter(clientes,
                    onEditarClickListener = { cliente ->
                        abrirEdicaoCliente(cliente)
                    },
                    onExcluirClickListener = { cliente ->
                        confirmarExclusao(cliente)
                    }
                )
                recyclerView.adapter = adapter
            }
        }
    }

    private fun abrirEdicaoCliente(cliente: Cliente) {
        val intent = Intent(this, EditarClienteActivity::class.java).apply {
            putExtra("CLIENTE_CPF", cliente.cpf)
        }
        startActivity(intent)
    }

    private fun confirmarExclusao(cliente: Cliente) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Deseja realmente excluir o cliente ${cliente.nome}?")
            .setPositiveButton("Excluir") { _, _ ->
                excluirCliente(cliente)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun excluirCliente(cliente: Cliente) {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoClientesActivity)
            db.clienteDAO().delete(cliente)
            carregarClientes()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarClientes()
    }
}