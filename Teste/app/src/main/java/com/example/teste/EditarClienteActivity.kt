package com.example.teste

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.teste.API.CepResponse
import com.example.teste.API.ViaCepService
import com.example.teste.database.AppDatabase
import com.example.teste.database.Cliente
import com.santalu.maskara.widget.MaskEditText
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class EditarClienteActivity : AppCompatActivity() {

    private lateinit var clienteOriginal: Cliente

    private lateinit var nomeInput: EditText
    private lateinit var cpfInput: EditText
    private lateinit var cepInput: MaskEditText
    private lateinit var endInput: EditText
    private lateinit var bairroInput: EditText
    private lateinit var cidadeInput: EditText
    private lateinit var tel1Input: EditText
    private lateinit var tel2Input: EditText
    private lateinit var emailInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_cliente)

        // Inicializa campos (sempre na UI Thread)
        nomeInput = findViewById(R.id.nomeCliente)
        cpfInput = findViewById(R.id.cpfCliente)
        cepInput = findViewById(R.id.cepCliente)
        endInput = findViewById(R.id.endCliente)
        bairroInput = findViewById(R.id.bairroCliente)
        cidadeInput = findViewById(R.id.cidCliente)
        tel1Input = findViewById(R.id.telClienteText8)
        tel2Input = findViewById(R.id.telCliente2)
        emailInput = findViewById(R.id.emailCliente)

        val cpfCliente = intent.getStringExtra("CLIENTE_CPF") ?: ""

        // Busca o cliente no banco (em background)
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@EditarClienteActivity)
            val cliente = db.clienteDAO().getClienteByCpf(cpfCliente)
            cliente?.let {
                clienteOriginal = it
                withContext(Dispatchers.Main) {
                    preencherCampos(it)
                }
            }
        }

        // Retrofit ViaCEP
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ViaCepService::class.java)

        cepInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val cepLimpo = cepInput.text.toString().filter { it.isDigit() }
                if (cepLimpo.length == 8) {
                    service.getEndereco(cepLimpo).enqueue(object : Callback<CepResponse> {
                        override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {
                            val body = response.body()
                            if (body != null && body.erro != true) {
                                if (endInput.text.isBlank()) endInput.setText(body.logradouro ?: "")
                                if (bairroInput.text.isBlank()) bairroInput.setText(body.bairro ?: "")
                                if (cidadeInput.text.isBlank()) cidadeInput.setText(body.localidade ?: "")
                            } else {
                                Toast.makeText(this@EditarClienteActivity, "CEP não encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<CepResponse>, t: Throwable) {
                            Toast.makeText(this@EditarClienteActivity, "Erro ao consultar CEP", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        findViewById<Button>(R.id.btnSalvarCliente).setOnClickListener { salvarAlteracoes() }
        findViewById<Button>(R.id.voltar_btn).setOnClickListener { finish() }
    }

    private fun preencherCampos(cliente: Cliente) {
        nomeInput.setText(cliente.nome)
        cpfInput.setText(cliente.cpf)
        endInput.setText(cliente.endereco)
        bairroInput.setText(cliente.bairro)
        cepInput.setText(cliente.cep)
        cidadeInput.setText(cliente.cidade)
        tel1Input.setText(cliente.telefone)
        tel2Input.setText(cliente.telefone2)
        emailInput.setText(cliente.email)
    }

    private fun salvarAlteracoes() {
        val clienteEditado = clienteOriginal.copy(
            nome = nomeInput.text.toString(),
            endereco = endInput.text.toString(),
            bairro = bairroInput.text.toString(),
            cep = cepInput.text.toString(),
            cidade = cidadeInput.text.toString(),
            telefone = tel1Input.text.toString(),
            telefone2 = tel2Input.text.toString(),
            email = emailInput.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@EditarClienteActivity)
            db.clienteDAO().update(clienteEditado)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EditarClienteActivity, "Cliente atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
