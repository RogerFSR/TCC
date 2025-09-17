package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teste.database.AppDatabase
import com.example.teste.database.Cliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditarClienteActivity : AppCompatActivity() {

    private lateinit var cliente: Cliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cliente)

        // Recebe o CPF do cliente a ser editado
        val cpfCliente = intent.getStringExtra("CLIENTE_CPF") ?: ""

        // Busca o cliente no banco de dados
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@EditarClienteActivity)
            cliente = db.clienteDAO().getClienteByCpf(cpfCliente) ?: return@launch

            // Preenche os campos na thread principal
            runOnUiThread {
                preencherCampos(cliente)
            }
        }

        // Configura os botões
        findViewById<Button>(R.id.btnSalvar).setOnClickListener { salvarAlteracoes() }
        findViewById<Button>(R.id.btnCancelar).setOnClickListener { finish() }
    }

    private fun preencherCampos(cliente: Cliente) {
        findViewById<EditText>(R.id.edtNome).setText(cliente.nome)
        findViewById<EditText>(R.id.edtCpf).setText(cliente.cpf)
        findViewById<EditText>(R.id.edtEndereco).setText(cliente.endereco)
        findViewById<EditText>(R.id.edtBairro).setText(cliente.bairro)
        findViewById<EditText>(R.id.edtCep).setText(cliente.cep)
        findViewById<EditText>(R.id.edtCidade).setText(cliente.cidade)
        findViewById<EditText>(R.id.edtTelefone).setText(cliente.telefone)
        findViewById<EditText>(R.id.edtTelefone2).setText(cliente.telefone2)
        findViewById<EditText>(R.id.edtEmail).setText(cliente.email)
    }

    private fun salvarAlteracoes() {
        // Cria objeto com os dados editados
        val clienteEditado = Cliente(
            cpf = findViewById<EditText>(R.id.edtCpf).text.toString(),
            nome = findViewById<EditText>(R.id.edtNome).text.toString(),
            endereco = findViewById<EditText>(R.id.edtEndereco).text.toString(),
            bairro = findViewById<EditText>(R.id.edtBairro).text.toString(),
            cep = findViewById<EditText>(R.id.edtCep).text.toString(),
            cidade = findViewById<EditText>(R.id.edtCidade).text.toString(),
            telefone = findViewById<EditText>(R.id.edtTelefone).text.toString(),
            telefone2 = findViewById<EditText>(R.id.edtTelefone2).text.toString(),
            email = findViewById<EditText>(R.id.edtEmail).text.toString()
        )

        // Salva no banco de dados
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@EditarClienteActivity)
            db.clienteDAO().update(clienteEditado)

            runOnUiThread {
                Toast.makeText(
                    this@EditarClienteActivity,
                    "Cliente atualizado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}