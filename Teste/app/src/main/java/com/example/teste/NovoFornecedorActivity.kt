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
import com.example.teste.API.CepResponse
import com.example.teste.API.ViaCepService
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

        // Ajuste das margens do sistema (status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Pega os campos do layout (usa os IDs que já estão no teu XML)
        etCep = findViewById(R.id.cepFornecedor)
        etLogradouro = findViewById(R.id.endFornecedor)
        etBairro = findViewById(R.id.bairroFornecedor)
        etCidade = findViewById(R.id.cidadeFornecedor)
        etUf = findViewById(R.id.estadoFornecedor)

        // Monitora mudanças no CEP
        etCep.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length == 8) { // CEP completo sem hífen
                    buscarEndereco(s.toString())
                } else {
                    limparCampos()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val btnVoltar = findViewById<Button>(R.id.btnVoltarFornecedor)
        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // 🔹 Agora essas funções estão DENTRO da classe
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
