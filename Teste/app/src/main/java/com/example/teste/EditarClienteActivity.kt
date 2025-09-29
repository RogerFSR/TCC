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
import java.util.*

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
        findViewById<Button>(R.id.btnSalvarCliente).setOnClickListener { salvarAlteracoes() }
        findViewById<Button>(R.id.voltar_btn).setOnClickListener { finish() }
    }

    private fun preencherCampos(cliente: Cliente) {
        findViewById<EditText>(R.id.nomeCliente).setText(cliente.nome)
        findViewById<EditText>(R.id.cpfCliente).setText(cliente.cpf)
        findViewById<EditText>(R.id.endCliente).setText(cliente.endereco)
        findViewById<EditText>(R.id.bairroCliente).setText(cliente.bairro)
        findViewById<EditText>(R.id.cepCliente).setText(cliente.cep)
        findViewById<EditText>(R.id.cidCliente).setText(cliente.cidade)
        findViewById<EditText>(R.id.telClienteText8).setText(cliente.telefone)
        findViewById<EditText>(R.id.telCliente2).setText(cliente.telefone2)
        findViewById<EditText>(R.id.emailCliente).setText(cliente.email)
    }

    private fun salvarAlteracoes() {
        // Cria objeto com os dados editados
        val clienteEditado = Cliente(
            cpf = findViewById<EditText>(R.id.cpfCliente).text.toString(),
            nome = findViewById<EditText>(R.id.nomeCliente).text.toString(),
            endereco = findViewById<EditText>(R.id.endCliente).text.toString(),
            bairro = findViewById<EditText>(R.id.bairroCliente).text.toString(),
            cep = findViewById<EditText>(R.id.cepCliente).text.toString(),
            cidade = findViewById<EditText>(R.id.cidCliente).text.toString(),
            telefone = findViewById<EditText>(R.id.telClienteText8).text.toString(),
            telefone2 = findViewById<EditText>(R.id.telCliente2).text.toString(),
            email = findViewById<EditText>(R.id.emailCliente).text.toString()
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