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
import com.example.teste.adapters.ProdutoAdapter
import com.example.teste.database.AppDatabase
import com.example.teste.database.Produto
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoEstoqueActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.consultar_estoque)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnEstoqueToMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupButtons()
        carregarProdutos()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProdutoAdapter(
            emptyList(),
            onEditarClickListener = { produto ->
                abrirEdicaoProduto(produto)
            },
            onExcluirClickListener = { produto ->
                confirmarExclusao(produto)
            },
            onEntradaEstoqueClickListener = { produto ->
                abrirEntradaEstoque(produto) // novo listener
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.list_to_main).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.novo_produto).setOnClickListener {
            startActivity(Intent(this, NovoProdutoActivity::class.java))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun carregarProdutos() {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoEstoqueActivity)
            val produtos = db.produtoDAO().getAllProdutos()

            launch(Dispatchers.Main) {
                adapter = ProdutoAdapter(
                    produtos,
                    onEditarClickListener = { produto ->
                        abrirEdicaoProduto(produto)
                    },
                    onExcluirClickListener = { produto ->
                        confirmarExclusao(produto)
                    },
                    onEntradaEstoqueClickListener = { produto ->
                        abrirEntradaEstoque(produto)
                    }
                )
                recyclerView.adapter = adapter
            }
        }
    }

    private fun abrirEdicaoProduto(produto: Produto) {
        val intent = Intent(this, EditarProdutoActivity::class.java).apply {
            putExtra("COD_BARRAS", produto.codigobarras)
        }
        startActivity(intent)
    }

    // 🔹 Novo método modelado (sem Activity real ainda)
    private fun abrirEntradaEstoque(produto: Produto) {
        val intent = Intent(this, /* TODO: Substituir pelo nome da Activity futura */ NovoLayoutActivity::class.java).apply {
            putExtra("COD_BARRAS", produto.codigobarras)
        }
        startActivity(intent)
    }

    private fun confirmarExclusao(produto: Produto) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Deseja realmente excluir o produto ${produto.nome}?")
            .setPositiveButton("Excluir") { _, _ ->
                excluirProduto(produto)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun excluirProduto(produto: Produto) {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@InfoEstoqueActivity)
            db.produtoDAO().delete(produto)
            carregarProdutos()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarProdutos()
    }
}
