package com.example.teste.utils

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.example.teste.database.AppDatabase
import com.example.teste.database.ProdutoVendidoComDescricao
import com.example.teste.database.Venda
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.text.DecimalFormat

object PdfHelper {

    private val df = DecimalFormat("0.00")

    suspend fun gerarPdfVendaPublicDocuments(context: Context, venda: Venda) {
        withContext(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(context)

                // Pega os itens da venda com a descrição correta
                val itens: List<ProdutoVendidoComDescricao> = db.produtoVendidoDAO()
                    .getItensComDescricaoByVenda(venda.nVenda)

                // Cria PDF em memória
                val byteArrayOutputStream = java.io.ByteArrayOutputStream()
                val writer = PdfWriter(byteArrayOutputStream)
                val pdf = PdfDocument(writer)
                val document = Document(pdf)

                // Cabeçalho
                document.add(
                    Paragraph("Relatório da Venda ID: ${venda.nVenda}")
                        .setBold()
                        .setFontSize(18f)
                        .setTextAlignment(TextAlignment.CENTER)
                )
                document.add(Paragraph("\n"))
                document.add(
                    Paragraph(
                        "Data/Hora: ${venda.data} ${venda.hora}\n" +
                                "Funcionário: ${venda.funcionario}\n" +
                                "Cliente: ${venda.cliente}\n" +
                                "Pagou: ${if (venda.pagou) "Sim" else "Não"}"
                    )
                )
                document.add(Paragraph("\n"))

                // Tabela
                val table = Table(UnitValue.createPercentArray(floatArrayOf(2f,5f,2f,3f,3f)))
                    .useAllAvailableWidth()

                val headers = listOf("Código","Descrição do Produto","Qntd","Valor Unitário (R$)","Valor Total (R$)")
                headers.forEach { h ->
                    val cell = Cell().add(Paragraph(h))
                    cell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    cell.setFontColor(ColorConstants.BLACK)
                    cell.setTextAlignment(TextAlignment.CENTER)
                    table.addHeaderCell(cell)
                }

                var totalGeral = 0.0
                if (itens.isNotEmpty()) {
                    itens.forEach { item ->
                        val row = listOf(
                            item.codigobarras,
                            item.descricao,
                            item.qtd.toString(),
                            df.format(item.valorvenda),
                            df.format(item.subtotal)
                        )
                        row.forEach { v ->
                            val cell = Cell().add(Paragraph(v))
                            cell.setBackgroundColor(ColorConstants.WHITE)
                            cell.setFontColor(ColorConstants.BLACK)
                            cell.setTextAlignment(TextAlignment.CENTER)
                            table.addCell(cell)
                        }
                        totalGeral += item.subtotal
                    }
                } else {
                    val row = listOf("-", "Nenhum item cadastrado", "-", "-", "0.00")
                    row.forEach { v ->
                        val cell = Cell().add(Paragraph(v))
                        cell.setBackgroundColor(ColorConstants.WHITE)
                        cell.setFontColor(ColorConstants.BLACK)
                        cell.setTextAlignment(TextAlignment.CENTER)
                        table.addCell(cell)
                    }
                }

                // Linha Total Geral
                val totalCell = Cell(1,4).add(Paragraph("Total Geral"))
                totalCell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
                totalCell.setFontColor(ColorConstants.BLACK)
                totalCell.setTextAlignment(TextAlignment.RIGHT)
                table.addCell(totalCell)

                val valorTotalCell = Cell().add(Paragraph(df.format(totalGeral)))
                valorTotalCell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
                valorTotalCell.setFontColor(ColorConstants.BLACK)
                valorTotalCell.setTextAlignment(TextAlignment.CENTER)
                table.addCell(valorTotalCell)

                document.add(table)
                document.close()

                // Salva no Documents público usando MediaStore
                val nomeArquivo = "Venda_${venda.nVenda}_${System.currentTimeMillis()}.pdf"
                val resolver = context.contentResolver

                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, nomeArquivo)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents")
                }

                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
                uri?.let {
                    val outStream: OutputStream? = resolver.openOutputStream(uri)
                    outStream?.use { stream ->
                        stream.write(byteArrayOutputStream.toByteArray())
                        stream.flush()
                    }
                    Log.d("PDF", "PDF gerado em Documents: $nomeArquivo")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
