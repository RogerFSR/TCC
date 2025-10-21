package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TiposActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tipos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bntNovoTipo = findViewById<Button>(R.id.add_tipo)
        bntNovoTipo.setOnClickListener{
            val intent = Intent(this, NovoTipoActivity::class.java)
            startActivity(intent)
        }

        val bntListarTipo = findViewById<Button>(R.id.listar_tipo)
        bntListarTipo.setOnClickListener{
            val intent = Intent(this, InfoTiposActivity::class.java)
            startActivity(intent)
        }

        val bntMain = findViewById<Button>(R.id.btn_voltar)
        bntMain.setOnClickListener {
            finish()
        }
    }
}