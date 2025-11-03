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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teste.adapters.VendaAdapter
import com.example.teste.database.AppDatabase
import com.example.teste.database.Produto
import com.example.teste.database.ProdutoVendido
import com.example.teste.database.Venda
import kotlinx.coroutines.Dispatchers
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
    private lateinit var edtCodigo: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VendaAdapter
    private val listaDeItens = mutableListOf<ProdutoVendido>()

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
        edtCodigo = findViewById(R.id.editTextText)
        val btnInserirManual = findViewById<Button>(R.id.btnInserirManual)
        btnInserirManual.setOnClickListener {
            val codigoManual = edtCodigo.text.toString().trim()
            if (codigoManual.isNotEmpty()) {
                adicionarProdutoPorCodigo(codigoManual)
                edtCodigo.text.clear()
            } else {
                Toast.makeText(this, "Digite um código antes de inserir", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView = findViewById(R.id.recyclerItensVenda)

        setupRecycler()
        startCamera()

        // Recebe dados da activity anterior
        idFuncionario = intent.getIntExtra("funcionario_id", -1)
        cpfCliente = intent.getStringExtra("cpf_cliente") ?: ""

        if (idFuncionario == -1 || cpfCliente.isBlank()) {
            Toast.makeText(this, "Erro ao carregar dados da venda", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<Button>(R.id.sale_to_main).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.confirm_sale).setOnClickListener {
            finalizarVenda()
        }
    }

    private fun setupRecycler() {
        adapter = VendaAdapter(listaDeItens) { /* callback vazio, sem total */ }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(cameraPreview.surfaceProvider)
            }

            val barcodeScanner = BarcodeScanning.getClient()

            val analysis = ImageAnalysis.Builder().build().also { analysisUseCase ->
                analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        barcodeScanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    val value = barcode.rawValue
                                    if (!value.isNullOrBlank()) {
                                        runOnUiThread {
                                            if (edtCodigo.text.toString() != value) {
                                                edtCodigo.setText(value)
                                                adicionarProdutoPorCodigo(value)
                                            }
                                        }
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

    private fun adicionarProdutoPorCodigo(codigo: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@RegistroVendasActivity)
            val produto: Produto? = db.produtoDAO().getProdutoByCodBarra(codigo)
            if (produto == null) {
                runOnUiThread {
                    Toast.makeText(this@RegistroVendasActivity, "Produto não encontrado: $codigo", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            val existente = listaDeItens.find { it.codigobarras == codigo }
            if (existente != null) {
                existente.qtd++
                existente.subtotal = existente.qtd * existente.valorvenda
                runOnUiThread { adapter.notifyDataSetChanged() }
                return@launch
            }

            val novo = ProdutoVendido(
                nVenda = 0,
                codigobarras = produto.codigobarras,
                qtd = 1,
                valorvenda = produto.preco,
                subtotal = produto.preco
            )

            listaDeItens.add(novo)
            runOnUiThread { adapter.notifyDataSetChanged() }
        }
    }

    private fun finalizarVenda() {
        if (listaDeItens.isEmpty()) {
            Toast.makeText(this, "Nenhum item na venda", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@RegistroVendasActivity)
            val vendaDao = db.vendaDAO()
            val produtoVendidoDao = db.produtoVendidoDAO()

            val dataAtual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val horaAtual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            val venda = Venda(
                nVenda = 0,
                data = dataAtual,
                hora = horaAtual,
                funcionario = idFuncionario,
                cliente = cpfCliente,
                pagou = true
            )

            val idVendaLong = vendaDao.insertAndReturnId(venda)
            val idVenda = idVendaLong.toInt()

            for (pv in listaDeItens) {
                val produtoVendido = pv.copy(nVenda = idVenda)
                produtoVendidoDao.insert(produtoVendido)
            }

            runOnUiThread {
                Toast.makeText(this@RegistroVendasActivity, "Venda registrada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
