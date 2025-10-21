package com.example.teste

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.teste.database.AppDatabase
import com.santalu.maskara.widget.MaskEditText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditarFornecedorActivity : AppCompatActivity() {

    private lateinit var cnpjFornecedor: MaskEditText
    private lateinit var nomeFornecedor: EditText
    private lateinit var telFornecedor: MaskEditText
    private lateinit var emailFornecedor: EditText
    private lateinit var cepFornecedor: MaskEditText
    private lateinit var endFornecedor: EditText
    private lateinit var bairroFornecedor: EditText
    private lateinit var cidadeFornecedor: EditText
    private lateinit var estadoFornecedor: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnVoltar: Button

    private lateinit var cnpj: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_fornecedor)

        cnpjFornecedor = findViewById(R.id.cnpjFornecedor)
        nomeFornecedor = findViewById(R.id.nomeFornecedor)
        telFornecedor = findViewById(R.id.telFornecedor)
        emailFornecedor = findViewById(R.id.emailFornecedor)
        cepFornecedor = findViewById(R.id.cepFornecedor)
        endFornecedor = findViewById(R.id.endFornecedor)
        bairroFornecedor = findViewById(R.id.bairroFornecedor)
        cidadeFornecedor = findViewById(R.id.cidadeFornecedor)
        estadoFornecedor = findViewById(R.id.estadoFornecedor)
        btnSalvar = findViewById(R.id.btnSalvarFornecedor)
        btnVoltar = findViewById(R.id.btnVoltarFornecedor)

        cnpj = intent.getStringExtra("CNPJ_FORNECEDOR") ?: ""

        if (cnpj.isNotBlank()) {
            carregarFornecedor()
        }

        btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun carregarFornecedor() {
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@EditarFornecedorActivity)
            val fornecedor = db.fornecedorDAO().getFornecedorByCnpjCpf(cnpj)

            fornecedor?.let {
                launch(Dispatchers.Main) {
                    cnpjFornecedor.setText(it.cnpj_cpf)
                    nomeFornecedor.setText(it.nome)
                    telFornecedor.setText(it.telefone)
                    emailFornecedor.setText(it.email)
                    cepFornecedor.setText(it.cep)
                    endFornecedor.setText(it.end)
                    bairroFornecedor.setText(it.bairro)
                    cidadeFornecedor.setText(it.cidade)
                    estadoFornecedor.setText(it.estado)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun salvarAlteracoes() {
        val nome = nomeFornecedor.text.toString()
        val tel = telFornecedor.text.toString()
        val email = emailFornecedor.text.toString()
        val cep = cepFornecedor.text.toString()
        val end = endFornecedor.text.toString()
        val bairro = bairroFornecedor.text.toString()
        val cidade = cidadeFornecedor.text.toString()
        val estado = estadoFornecedor.text.toString()

        if (nome.isBlank()) {
            Toast.makeText(this, "O nome é obrigatório!", Toast.LENGTH_SHORT).show()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@EditarFornecedorActivity)
            val fornecedorExistente = db.fornecedorDAO().getFornecedorByCnpjCpf(cnpj)

            if (fornecedorExistente != null) {
                val fornecedorAtualizado = fornecedorExistente.copy(
                    nome = nome,
                    telefone = tel,
                    email = email,
                    cep = cep,
                    end = end,
                    bairro = bairro,
                    cidade = cidade,
                    estado = estado
                )

                db.fornecedorDAO().update(fornecedorAtualizado)

                launch(Dispatchers.Main) {
                    Toast.makeText(
                        this@EditarFornecedorActivity,
                        "Fornecedor atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}
