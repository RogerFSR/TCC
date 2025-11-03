package com.example.teste

import android.content.Intent
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.teste.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class NovoProdutoActivity : AppCompatActivity() {

    private lateinit var spinnerTipo: Spinner
    private lateinit var spinnerFornecedor: Spinner
    private var listaTipos = listOf<Tipo>()
    private var listaFornecedores = listOf<Fornecedor>()
    private lateinit var edtPreco: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.novo_produto)

        // Ajuste de bordas pelo sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnVoltar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerTipo = findViewById(R.id.spinnerTipo)
        spinnerFornecedor = findViewById(R.id.spinnerFornecedor)
        edtPreco = findViewById(R.id.edtPreco)

        carregarTipos()
        carregarFornecedores()

        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            salvarProduto()
        }

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            if (camposPreenchidos()) {
                AlertDialog.Builder(this)
                    .setTitle("Descartar alterações?")
                    .setMessage("Você tem alterações não salvas. Deseja realmente voltar?")
                    .setPositiveButton("Sim") { _, _ -> finish() }
                    .setNegativeButton("Não", null)
                    .show()
            } else {
                finish()
            }
        }

        findViewById<Button>(R.id.add_tipo).setOnClickListener {
            startActivity(Intent(this, TiposActivity::class.java))
        }

        findViewById<Button>(R.id.fornecedores).setOnClickListener {
            startActivity(Intent(this, FornecedoresActivity::class.java))
        }

        findViewById<Button>(R.id.estoque).setOnClickListener {
            startActivity(Intent(this, InfoEstoqueActivity::class.java))
        }
    }

    private fun camposPreenchidos(): Boolean {
        return findViewById<EditText>(R.id.edtCodigoBarras).text.isNotEmpty() ||
                findViewById<EditText>(R.id.edtNome).text.isNotEmpty() ||
                findViewById<EditText>(R.id.edtEstoqueMin).text.isNotEmpty() ||
                findViewById<EditText>(R.id.edtEstoqueAtual).text.isNotEmpty() ||
                findViewById<EditText>(R.id.edtEstoqueMax).text.isNotEmpty() ||
                edtPreco.text.isNotEmpty()
    }

    private fun carregarTipos() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@NovoProdutoActivity)
            listaTipos = db.tipoDAO().getAllTipos()

            runOnUiThread {
                val adapter = ArrayAdapter(
                    this@NovoProdutoActivity,
                    android.R.layout.simple_spinner_item,
                    listaTipos.map { it.descricao }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTipo.adapter = adapter
            }
        }
    }

    private fun carregarFornecedores() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@NovoProdutoActivity)
            listaFornecedores = db.fornecedorDAO().getAllFornecedores()

            runOnUiThread {
                val adapter = ArrayAdapter(
                    this@NovoProdutoActivity,
                    android.R.layout.simple_spinner_item,
                    listaFornecedores.map { it.nome }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFornecedor.adapter = adapter
            }
        }
    }

    private fun salvarProduto() {
        val codigoBarras = findViewById<EditText>(R.id.edtCodigoBarras).text.toString().trim()
        val nome = findViewById<EditText>(R.id.edtNome).text.toString().trim()
        val geladeira = findViewById<CheckBox>(R.id.cbGeladeira).isChecked
        val estoqueMin = findViewById<EditText>(R.id.edtEstoqueMin).text.toString().toIntOrNull() ?: 0
        val estoqueAtual = findViewById<EditText>(R.id.edtEstoqueAtual).text.toString().toIntOrNull() ?: 0
        val estoqueMax = findViewById<EditText>(R.id.edtEstoqueMax).text.toString().toIntOrNull() ?: 0
        val preco = edtPreco.text.toString().replace(",", ".").toDoubleOrNull()

        // Validações
        when {
            codigoBarras.isEmpty() -> {
                Toast.makeText(this, "Informe o código de barras", Toast.LENGTH_SHORT).show()
                return
            }
            nome.isEmpty() -> {
                Toast.makeText(this, "Informe o nome do produto", Toast.LENGTH_SHORT).show()
                return
            }
            preco == null -> {
                Toast.makeText(this, "Informe um preço válido", Toast.LENGTH_SHORT).show()
                return
            }
            listaTipos.isEmpty() -> {
                Toast.makeText(this, "Nenhum tipo cadastrado", Toast.LENGTH_SHORT).show()
                return
            }
            listaFornecedores.isEmpty() -> {
                Toast.makeText(this, "Nenhum fornecedor cadastrado", Toast.LENGTH_SHORT).show()
                return
            }
            estoqueMin > estoqueMax -> {
                Toast.makeText(this, "Estoque mínimo não pode ser maior que o máximo", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val tipoSelecionado = listaTipos[spinnerTipo.selectedItemPosition]
        val fornecedorSelecionado = listaFornecedores[spinnerFornecedor.selectedItemPosition]

        val produto = Produto(
            codigobarras = codigoBarras,
            nome = nome,
            tipo = tipoSelecionado.codtipo,
            geladeira = geladeira,
            fornecedor = fornecedorSelecionado.cnpj_cpf,
            estoque_min = estoqueMin,
            estoque_atual = estoqueAtual,
            estoque_max = estoqueMax,
            preco = preco!! // agora não nulo, safe porque já validamos acima
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@NovoProdutoActivity)
            try {
                val produtoExistente = db.produtoDAO().getProdutoByCodBarra(codigoBarras)
                if (produtoExistente != null) {
                    runOnUiThread {
                        Toast.makeText(
                            this@NovoProdutoActivity,
                            "Já existe um produto com este código de barras",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return@launch
                }
                db.produtoDAO().insert(produto)
                runOnUiThread {
                    Toast.makeText(
                        this@NovoProdutoActivity,
                        "Produto salvo com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@NovoProdutoActivity,
                        "Erro ao salvar produto: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
