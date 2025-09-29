package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.teste.API.CepResponse
import com.example.teste.API.ViaCepService
import com.example.teste.database.AppDatabase
import com.example.teste.database.Cliente
import com.santalu.maskara.widget.MaskEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class NovoClienteActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var nomeInput: EditText
    private lateinit var cpfInput: MaskEditText
    private lateinit var telInput: MaskEditText
    private lateinit var telInput2: MaskEditText
    private lateinit var cepInput: MaskEditText
    private lateinit var endInput: EditText
    private lateinit var bairroInput: EditText
    private lateinit var cidadeInput: EditText
    private lateinit var emailInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.novo_cliente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)

        nomeInput = findViewById(R.id.nomeCliente)
        cpfInput = findViewById(R.id.cpfCliente)
        telInput = findViewById(R.id.telClienteText8)
        telInput2 = findViewById(R.id.telCliente2)
        cepInput = findViewById(R.id.cepCliente)
        endInput = findViewById(R.id.endCliente)
        bairroInput = findViewById(R.id.bairroCliente)
        cidadeInput = findViewById(R.id.cidCliente)
        emailInput = findViewById(R.id.emailCliente)

        val btnSalvar = findViewById<Button>(R.id.btnSalvarCliente)
        val btnVoltar = findViewById<Button>(R.id.voltar_btn)

        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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
                                Toast.makeText(this@NovoClienteActivity, "CEP não encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<CepResponse>, t: Throwable) {
                            Toast.makeText(this@NovoClienteActivity, "Erro ao consultar CEP", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        btnSalvar.setOnClickListener {
            val cpf = cpfInput.text.toString()
            val email = emailInput.text.toString()
            val telefone = telInput.text.toString()
            val cep = cepInput.text.toString()

            // Validações simples (ajuste regex conforme necessidade)
            if (!cpf.matches(Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"))) {
                Toast.makeText(this, "CPF inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!email.contains("@") || !email.contains(".")) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!telefone.matches(Regex("\\(\\d{2}\\) \\d{4,5}-\\d{4}"))) {
                Toast.makeText(this, "Telefone inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!cep.matches(Regex("\\d{5}-\\d{3}"))) {
                Toast.makeText(this, "CEP inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cliente = Cliente(
                cpf = cpfInput.text.toString().filter { it.isDigit() }, // se seu PK exige sem pontuação, adapte
                nome = nomeInput.text.toString(),
                endereco = endInput.text.toString(),
                bairro = bairroInput.text.toString(),
                cep = cepInput.text.toString(),
                cidade = cidadeInput.text.toString(),
                telefone = telInput.text.toString(),
                telefone2 = telInput2.text.toString(),
                email = emailInput.text.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    db.clienteDAO().insert(cliente)
                    runOnUiThread {
                        Toast.makeText(this@NovoClienteActivity, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        limparCampos()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@NovoClienteActivity, "Erro ao salvar cliente: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun limparCampos() {
        nomeInput.text.clear()
        cpfInput.text?.clear()
        telInput.text?.clear()
        telInput2.text?.clear()
        cepInput.text?.clear()
        endInput.text.clear()
        bairroInput.text.clear()
        cidadeInput.text.clear()
        emailInput.text.clear()
    }
}
