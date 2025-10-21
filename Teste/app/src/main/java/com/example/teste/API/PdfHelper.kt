package com.example.teste.utils

import android.content.Context
import android.os.Environment
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.example.teste.database.Venda
import com.example.teste.database.AppDatabase
import com.itextpdf.kernel.pdf.PdfName.App
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.DecimalFormat

object PdfHelper {

    private val df = DecimalFormat("0.00")

    fun gerarPdfVenda(context: Context, venda: Venda) {
        runBlocking {
            try {
                val db = AppDatabase.getDatabase(context)
                val itens = db.itemVendaDAO().getItensByVenda(venda.nVenda)

                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "Venda_${venda.nVenda}.pdf"
                )
                val writer = PdfWriter(file)
                val pdf = PdfDocument(writer)
                val document = Document(pdf)

                // -----------------------------
                // Informações da Venda
                // -----------------------------
                document.add(
                    Paragraph("Relatório da Venda")
                        .setBold()
                        .setFontSize(20f)
                        .setTextAlignment(TextAlignment.CENTER)
                )

                document.add(Paragraph("\n"))

                val infoVenda = """
                    ID Venda: ${venda.nVenda}
                    Data/Hora: ${venda.data} ${venda.hora}
                    Funcionário ID: ${venda.funcionario}
                    Cliente: ${venda.cliente}
                    Pagou: ${if (venda.pagou) "Sim" else "Não"}
                """.trimIndent()

                document.add(Paragraph(infoVenda).setFontSize(12f))
                document.add(Paragraph("\n"))

                // -----------------------------
                // Tabela de Itens
                // -----------------------------
                val table = Table(UnitValue.createPercentArray(floatArrayOf(2f, 5f, 2f, 3f, 3f)))
                    .useAllAvailableWidth()

                val headers = listOf("Código", "Descrição do Produto", "Qntd", "Valor Unitário (R$)", "Valor Total (R$)")
                headers.forEach { header ->
                    val cell = Cell().add(Paragraph(header))
                    cell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    cell.setFontColor(ColorConstants.BLACK)
                    cell.setTextAlignment(TextAlignment.CENTER)
                    table.addHeaderCell(cell)
                }

                // Linhas dos itens
                var totalGeral = 0.0
                itens.forEach { item ->
                    val row = listOf(
                        item.codigo,
                        item.descricao,
                        item.quantidade.toString(),
                        df.format(item.valorUnitario),
                        df.format(item.valorTotal)
                    )
                    row.forEach { value ->
                        val cell = Cell().add(Paragraph(value))
                        cell.setBackgroundColor(ColorConstants.WHITE)
                        cell.setFontColor(ColorConstants.BLACK)
                        cell.setTextAlignment(TextAlignment.CENTER)
                        table.addCell(cell)
                    }
                    totalGeral += item.valorTotal
                }

                // Linha de total geral
                val totalCell = Cell(1, 4).add(Paragraph("Total Geral"))
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

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
