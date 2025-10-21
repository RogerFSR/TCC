package com.example.teste

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditarProdutoActivity : AppCompatActivity() {

    private lateinit var nomeProduto: EditText
    private lateinit var codigoBarrasProduto: EditText
    private lateinit var quantidadeProduto: EditText
    private lateinit var precoProduto: EditText
    private lateinit var descricaoProduto: EditText
    private lateinit var fornecedorProduto: EditText

    private lateinit var btnSalvar: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_produto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referência dos campos
        nomeProduto = findViewById(R.id.nomeProduto)
        codigoBarrasProduto = findViewById(R.id.codigoBarrasProduto)
        quantidadeProduto = findViewById(R.id.quantidadeProduto)
        precoProduto = findViewById(R.id.precoProduto)
        descricaoProduto = findViewById(R.id.descricaoProduto)
        fornecedorProduto = findViewById(R.id.fornecedorProduto)

        btnSalvar = findViewById(R.id.btnSalvarProduto)
        btnVoltar = findViewById(R.id.btnVoltarProduto)

        // --- BOTÃO SALVAR ---
        btnSalvar.setOnClickListener {
            val nome = nomeProduto.text.toString().trim()
            val codigoBarras = codigoBarrasProduto.text.toString().trim()
            val quantidade = quantidadeProduto.text.toString().trim()
            val preco = precoProduto.text.toString().trim()
            val descricao = descricaoProduto.text.toString().trim()
            val fornecedor = fornecedorProduto.text.toString().trim()

            if (nome.isEmpty() || codigoBarras.isEmpty() || quantidade.isEmpty() ||
                preco.isEmpty() || descricao.isEmpty() || fornecedor.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                // Aqui você pode salvar no banco de dados (Room, por exemplo)
                Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                finish() // Fecha a tela
            }
        }

        // --- BOTÃO VOLTAR ---
        btnVoltar.setOnClickListener {
            finish() // Fecha a tela e volta pra anterior
        }
    }
}
