package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.teste.database.Fornecedor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NovoFornecedorActivity : AppCompatActivity() {

    private lateinit var etCep: EditText
    private lateinit var etLogradouro: EditText
    private lateinit var etBairro: EditText
    private lateinit var etCidade: EditText
    private lateinit var etUf: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.novo_fornecedor)

        // Ajusta margens do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etCep = findViewById(R.id.cepFornecedor)
        etLogradouro = findViewById(R.id.endFornecedor)
        etBairro = findViewById(R.id.bairroFornecedor)
        etCidade = findViewById(R.id.cidadeFornecedor)
        etUf = findViewById(R.id.estadoFornecedor)

        // Máscara de CEP: 12345-678
        etCep.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val mask = "#####-###"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return

                val str = s.toString().replace("-", "").trim()
                val masked = StringBuilder()

                var i = 0
                for (m in mask) {
                    if (m != '#' && i < str.length) {
                        masked.append(m)
                    } else if (i < str.length) {
                        masked.append(str[i])
                        i++
                    }
                }

                isUpdating = true
                etCep.setText(masked)
                etCep.setSelection(masked.length)
                isUpdating = false

                // dispara a busca apenas quando o CEP estiver completo
                if (str.length == 8) {
                    buscarEndereco(str)
                } else {
                    limparCampos()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val btnSalvar = findViewById<Button>(R.id.btnSalvarFornecedor)
        btnSalvar.setOnClickListener {
            salvarFornecedor()
        }

        val btnVoltar = findViewById<Button>(R.id.btnVoltarFornecedor)
        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun salvarFornecedor() {
        val cnpj = findViewById<EditText>(R.id.cnpjFornecedor).text.toString().replace(".", "").replace("/", "").replace("-", "")
        val nome = findViewById<EditText>(R.id.nomeFornecedor).text.toString().trim()
        val telefone = findViewById<EditText>(R.id.telFornecedor).text.toString().trim()
        val email = findViewById<EditText>(R.id.emailFornecedor).text.toString().trim()
        val cep = findViewById<EditText>(R.id.cepFornecedor).text.toString().trim()
        val endereco = findViewById<EditText>(R.id.endFornecedor).text.toString().trim()
        val bairro = findViewById<EditText>(R.id.bairroFornecedor).text.toString().trim()
        val cidade = findViewById<EditText>(R.id.cidadeFornecedor).text.toString().trim()
        val estado = findViewById<EditText>(R.id.estadoFornecedor).text.toString().trim()

        if (cnpj.isEmpty() || nome.isEmpty()) {
            Toast.makeText(this, "CNPJ e Nome são obrigatórios!", Toast.LENGTH_SHORT).show()
            return
        }

        val fornecedor = Fornecedor(
            cnpj_cpf = cnpj,
            nome = nome,
            telefone = telefone,
            email = email,
            cep = cep,
            end = endereco,
            bairro = bairro,
            cidade = cidade,
            estado = estado
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(this@NovoFornecedorActivity)
                db.fornecedorDAO().insert(fornecedor)
                runOnUiThread {
                    Toast.makeText(this@NovoFornecedorActivity, "Fornecedor salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // volta para a tela anterior
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@NovoFornecedorActivity, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun buscarEndereco(cep: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ViaCepService::class.java)
        val call = service.getEndereco(cep)

        call.enqueue(object : Callback<CepResponse> {
            override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {
                if (response.isSuccessful) {
                    val endereco = response.body()
                    if (endereco != null && endereco.erro != true) {
                        etLogradouro.setText(endereco.logradouro ?: "")
                        etBairro.setText(endereco.bairro ?: "")
                        etCidade.setText(endereco.localidade ?: "")
                        etUf.setText(endereco.uf ?: "")
                    } else {
                        Toast.makeText(this@NovoFornecedorActivity, "CEP inválido!", Toast.LENGTH_SHORT).show()
                        limparCampos()
                    }
                } else {
                    Toast.makeText(this@NovoFornecedorActivity, "Erro ao buscar CEP!", Toast.LENGTH_SHORT).show()
                    limparCampos()
                }
            }

            override fun onFailure(call: Call<CepResponse>, t: Throwable) {
                Toast.makeText(this@NovoFornecedorActivity, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
                limparCampos()
            }
        })
    }

    private fun limparCampos() {
        etLogradouro.text.clear()
        etBairro.text.clear()
        etCidade.text.clear()
        etUf.text.clear()
    }
}
