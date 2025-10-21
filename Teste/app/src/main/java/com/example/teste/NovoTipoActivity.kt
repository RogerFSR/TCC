package com.example.teste

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.teste.database.AppDatabase
import com.example.teste.database.Tipo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NovoTipoActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var descricaoInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.novo_tipo)

        db = AppDatabase.getDatabase(this)
        descricaoInput = findViewById(R.id.descricaoTipo)

        val btnSalvar = findViewById<Button>(R.id.btnSalvarTipo)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarTipo)

        btnVoltar.setOnClickListener {
            finish()
        }

        btnSalvar.setOnClickListener {
            val descricao = descricaoInput.text.toString().trim()

            if (descricao.isBlank()) {
                Toast.makeText(this, "A descrição é obrigatória!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipo = Tipo(
                codtipo = 0, // autoGenerate, então 0
                descricao = descricao
            )

            lifecycleScope.launch(Dispatchers.IO) {
                db.tipoDAO().insert(tipo)

                runOnUiThread {
                    Toast.makeText(this@NovoTipoActivity, "Tipo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    descricaoInput.text.clear()
                }
            }
        }
    }
}
