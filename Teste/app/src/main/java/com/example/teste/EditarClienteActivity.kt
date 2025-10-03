package com.example.teste

import android.content.Intent
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EditarClienteActivity : AppCompatActivity() {

    private lateinit var cliente: Cliente
    private lateinit var cepInput: MaskEditText
    private lateinit var endInput: EditText
    private lateinit var bairroInput: EditText
    private lateinit var cidadeInput: EditText


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_editar_cliente)

        // Recebe o CPF do cliente a ser editado
        val cpfCliente = intent.getStringExtra("CLIENTE_CPF") ?: ""

        // Busca o cliente no banco de dados
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@EditarClienteActivity)
            cliente = db.clienteDAO().getClienteByCpf(cpfCliente) ?: return@launch
            cepInput = findViewById(R.id.cepCliente)
            endInput = findViewById(R.id.endCliente)
            bairroInput = findViewById(R.id.bairroCliente)
            cidadeInput = findViewById(R.id.cidCliente)

            // Preenche os campos na thread principal
            runOnUiThread {
                preencherCampos(cliente)
            }
        }

        // Retrofit ViaCEP
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ViaCepService::class.java)

        // Quando perder foco do campo CEP, busca o endereço
        cepInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val cepLimpo = cepInput.text.toString().filter { it.isDigit() }
                if (cepLimpo.length == 8) {
                    service.getEndereco(cepLimpo).enqueue(object: Callback<CepResponse> {
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



    @OptIn(DelicateCoroutinesApi::class)
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