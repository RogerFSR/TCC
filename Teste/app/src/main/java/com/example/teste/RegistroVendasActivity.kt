package com.example.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.adapters.VendaAdapter
import com.example.teste.database.AppDatabase
import com.example.teste.database.ItemVenda
import com.example.teste.database.ProdutoVendido
import com.example.teste.database.Venda
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.core.Preview
import androidx.camera.core.ImageAnalysis
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class RegistroVendasActivity : AppCompatActivity() {

    private var idFuncionario: Int = -1
    private lateinit var cpfCliente: String
    private lateinit var cameraPreview: PreviewView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.registro_vendas)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnInfosToMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cameraPreview = findViewById(R.id.cameraPreview)

        startCamera()

        idFuncionario = intent.getIntExtra("funcionario_id", -1)
        cpfCliente = intent.getStringExtra("cpf_cliente") ?: ""

        if (idFuncionario == -1 || cpfCliente.isBlank()) {
            Toast.makeText(this, "Erro ao carregar dados da venda", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Botão de voltar ao menu principal
        val btnListToMain = findViewById<Button>(R.id.sale_to_main)
        btnListToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botão de finalizar venda
        val btnFinalizar = findViewById<Button>(R.id.confirm_sale)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerItensVenda)
        val listaDeItens = mutableListOf<ItemVenda>()

        val adapter = VendaAdapter(listaDeItens)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.notifyDataSetChanged()

        btnFinalizar.setOnClickListener {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@RegistroVendasActivity)
                val vendaDao = db.vendaDAO()
                val produtoVendidoDao = db.produtoVendidoDAO()

                val dataAtual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val horaAtual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                // 1. Criar e salvar a venda
                val venda = Venda(
                    nVenda = 0,
                    data = dataAtual,
                    hora = horaAtual,
                    funcionario = idFuncionario,
                    cliente = cpfCliente,
                    pagou = false
                )
                val idVenda = vendaDao.insertAndReturnId(venda)  // esse método você precisa implementar

                // 2. Salvar os produtos vendidos
                for (item in listaDeItens) {
                    val subtotal = item.quantidade * item.valorUnitario

                    val produtoVendido = ProdutoVendido(
                        nVenda = idVenda.toInt(),
                        codigobarras = item.codigobarras,
                        qtd = item.quantidade,
                        valorvenda = item.valorUnitario,
                        subtotal = subtotal
                    )
                    produtoVendidoDao.insert(produtoVendido)
                }

                Toast.makeText(this@RegistroVendasActivity, "Venda registrada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(cameraPreview.surfaceProvider)
            }

            val barcodeScanner = BarcodeScanning.getClient()

            val analysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                            barcodeScanner.process(image)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        val value = barcode.rawValue
                                        if (!value.isNullOrBlank()) {
                                            findViewById<EditText>(R.id.editTextText).setText(value)
                                        }
                                    }
                                }
                                .addOnCompleteListener { imageProxy.close() }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, analysis)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }
}
