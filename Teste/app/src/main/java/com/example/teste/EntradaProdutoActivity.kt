package com.example.teste

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.teste.database.AppDatabase
import com.example.teste.database.ProdutoEntrada
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EntradaProdutoActivity : AppCompatActivity() {

    private lateinit var inputNEntrada: EditText
    private lateinit var inputCodBarras: EditText
    private lateinit var inputQtd: EditText
    private lateinit var inputValorCompra: EditText
    private lateinit var inputSubtotal: EditText
    private lateinit var inputLote: EditText
    private lateinit var inputValidade: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.entrada_produto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarCampos()
        configurarBotoes()
    }

    private fun inicializarCampos() {
        inputNEntrada = findViewById(R.id.inputNEntrada)
        inputCodBarras = findViewById(R.id.inputCodBarras)
        inputQtd = findViewById(R.id.inputQtd)
        inputValorCompra = findViewById(R.id.inputValorCompra)
        inputSubtotal = findViewById(R.id.inputSubtotal)
        inputLote = findViewById(R.id.inputLote)
        inputValidade = findViewById(R.id.inputValidade)
        btnSalvar = findViewById(R.id.btnSalvarEntrada)
        btnVoltar = findViewById(R.id.btnVoltarEntrada)
    }

    private fun configurarBotoes() {
        btnSalvar.setOnClickListener {
            salvarEntradaProduto()
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun salvarEntradaProduto() {
        val nentrada = inputNEntrada.text.toString().toIntOrNull()
        val codigobarras = inputCodBarras.text.toString()
        val qtd = inputQtd.text.toString().toIntOrNull()
        val valorcompra = inputValorCompra.text.toString().toDoubleOrNull()
        val subtotal = inputSubtotal.text.toString().toDoubleOrNull()
        val lote = inputLote.text.toString()
        val validade = inputValidade.text.toString()

        if (nentrada == null || codigobarras.isEmpty() || qtd == null ||
            valorcompra == null || subtotal == null || lote.isEmpty() || validade.isEmpty()
        ) {
            Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        val produtoEntrada = ProdutoEntrada(
            nentrada = nentrada,
            codigobarras = codigobarras,
            qtd = qtd,
            valorcompra = valorcompra,
            subtotal = subtotal,
            lote = lote,
            validade = validade
        )

        // 🔧 Lógica de inserção no banco de dados — pronta para completar
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@EntradaProdutoActivity)
            db.produtoEntradaDAO().insert(produtoEntrada)
            launch(Dispatchers.Main) {
                Toast.makeText(this@EntradaProdutoActivity, "Entrada registrada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
