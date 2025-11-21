package com.emergencia.medica

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import org.json.JSONObject
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.kernel.colors.ColorConstants
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ScanQRCodeActivity : AppCompatActivity() {
    
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var tvScannedData: TextView
    private lateinit var btnGeneratePDF: Button
    
    private val CAMERA_PERMISSION_CODE = 100
    private val STORAGE_PERMISSION_CODE = 101
    
    private var scannedJsonData: JSONObject? = null
    private var formattedText: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qrcode)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Escanear QR Code"
        
        barcodeView = findViewById(R.id.barcodeView)
        tvScannedData = findViewById(R.id.tvScannedData)
        btnGeneratePDF = findViewById(R.id.btnGeneratePDF)
        
        btnGeneratePDF.setOnClickListener {
            checkStoragePermissionAndGeneratePDF()
        }
        
        if (checkCameraPermission()) {
            startScanning()
        } else {
            requestCameraPermission()
        }
    }
    
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true // No Android 10+, não precisa de permissão para salvar em Downloads
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    private fun checkStoragePermissionAndGeneratePDF() {
        if (checkStoragePermission()) {
            generatePDF()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }
    
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning()
                } else {
                    Toast.makeText(
                        this,
                        "Permissão de câmera necessária para escanear QR Code",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generatePDF()
                } else {
                    Toast.makeText(
                        this,
                        "Permissão de armazenamento necessária para salvar PDF",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    
    private fun startScanning() {
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    barcodeView.pause()
                    processScannedData(it.text)
                }
            }
        })
    }
    
    private fun processScannedData(data: String) {
        try {
            val json = JSONObject(data)
            scannedJsonData = json
            
            formattedText = buildString {
                appendLine("🆘 INFORMAÇÕES DE EMERGÊNCIA MÉDICA")
                appendLine("=" .repeat(40))
                appendLine()
                
                appendLine("👤 DADOS PESSOAIS:")
                appendLine("Nome: ${json.optString("fullName", "N/A")}")
                appendLine("Idade: ${json.optInt("age", 0)} anos")
                val bloodType = json.optString("bloodType", "")
                if (bloodType.isNotEmpty()) {
                    appendLine("Tipo Sanguíneo: $bloodType")
                }
                appendLine()
                
                val conditions = json.optString("preExistingConditions", "")
                if (conditions.isNotEmpty()) {
                    appendLine("🏥 CONDIÇÕES PRÉ-EXISTENTES:")
                    appendLine(conditions)
                    appendLine()
                }
                
                val medications = json.optString("currentMedications", "")
                if (medications.isNotEmpty()) {
                    appendLine("💊 MEDICAÇÕES EM USO:")
                    appendLine(medications)
                    appendLine()
                }
                
                val allergies = json.optString("allergies", "")
                if (allergies.isNotEmpty()) {
                    appendLine("⚠️ ALERGIAS:")
                    appendLine(allergies)
                    appendLine()
                }
                
                val hasPacemaker = json.optBoolean("hasPacemaker", false)
                val implants = json.optString("hasImplants", "")
                if (hasPacemaker || implants.isNotEmpty()) {
                    appendLine("⚡ DISPOSITIVOS/IMPLANTES:")
                    if (hasPacemaker) appendLine("• Marca-passo instalado")
                    if (implants.isNotEmpty()) appendLine("• $implants")
                    appendLine()
                }
                
                appendLine("📞 CONTATOS DE EMERGÊNCIA:")
                val contact1Name = json.optString("emergencyContact1Name", "")
                val contact1Phone = json.optString("emergencyContact1Phone", "")
                if (contact1Name.isNotEmpty()) {
                    appendLine("1. $contact1Name - $contact1Phone")
                }
                
                val contact2Name = json.optString("emergencyContact2Name", "")
                val contact2Phone = json.optString("emergencyContact2Phone", "")
                if (contact2Name.isNotEmpty()) {
                    appendLine("2. $contact2Name - $contact2Phone")
                }
                appendLine()
                
                val notes = json.optString("additionalNotes", "")
                if (notes.isNotEmpty()) {
                    appendLine("📝 OBSERVAÇÕES ADICIONAIS:")
                    appendLine(notes)
                }
            }
            
            tvScannedData.text = formattedText
            btnGeneratePDF.visibility = android.view.View.VISIBLE
            Toast.makeText(this, "QR Code lido com sucesso!", Toast.LENGTH_SHORT).show()
            
        } catch (e: Exception) {
            tvScannedData.text = "Dados não formatados:\n\n$data"
            btnGeneratePDF.visibility = android.view.View.GONE
            Toast.makeText(this, "QR Code não é de dados médicos", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }
    
    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
    
    private fun generatePDF() {
        try {
            val json = scannedJsonData ?: run {
                Toast.makeText(this, "Nenhum dado disponível para gerar PDF", Toast.LENGTH_SHORT).show()
                return
            }
            
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val safeName = json.optString("fullName", "Paciente")
                .replace(" ", "_")
                .replace(Regex("[^a-zA-Z0-9_]"), "")
            val fileName = "EmergenciaMedica_${safeName}_$timestamp.pdf"
            
            // Criar PDF no diretório de arquivos do app
            val tempFile = File(getExternalFilesDir(null), fileName)
            val writer = PdfWriter(tempFile)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)
            
            // Título
            val title = Paragraph()
                .add(Text("INFORMAÇÕES DE EMERGÊNCIA MÉDICA\n").setBold().setFontSize(18f))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.RED)
            document.add(title)
            
            document.add(Paragraph("\n"))
            
            // Data e hora de geração
            val dateTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            document.add(Paragraph("Gerado em: $dateTime").setFontSize(10f))
            document.add(Paragraph("_" .repeat(60)).setFontSize(8f))
            
            // Dados Pessoais
            document.add(Paragraph("\nDADOS PESSOAIS").setBold().setFontSize(14f))
            document.add(Paragraph("Nome: ${json.optString("fullName", "N/A")}"))
            document.add(Paragraph("Idade: ${json.optInt("age", 0)} anos"))
            val bloodType = json.optString("bloodType", "")
            if (bloodType.isNotEmpty()) {
                document.add(Paragraph("Tipo Sanguíneo: $bloodType").setBold().setFontColor(ColorConstants.RED))
            }
            
            // Condições Pré-existentes
            val conditions = json.optString("preExistingConditions", "")
            if (conditions.isNotEmpty()) {
                document.add(Paragraph("\nCONDIÇÕES PRÉ-EXISTENTES").setBold().setFontSize(14f))
                document.add(Paragraph(conditions))
            }
            
            // Medicações
            val medications = json.optString("currentMedications", "")
            if (medications.isNotEmpty()) {
                document.add(Paragraph("\nMEDICAÇÕES EM USO").setBold().setFontSize(14f))
                document.add(Paragraph(medications))
            }
            
            // Alergias
            val allergies = json.optString("allergies", "")
            if (allergies.isNotEmpty()) {
                document.add(Paragraph("\nALERGIAS").setBold().setFontSize(14f).setFontColor(ColorConstants.RED))
                document.add(Paragraph(allergies).setFontColor(ColorConstants.RED))
            }
            
            // Dispositivos/Implantes
            val hasPacemaker = json.optBoolean("hasPacemaker", false)
            val implants = json.optString("hasImplants", "")
            if (hasPacemaker || implants.isNotEmpty()) {
                document.add(Paragraph("\nDISPOSITIVOS/IMPLANTES").setBold().setFontSize(14f))
                if (hasPacemaker) {
                    document.add(Paragraph("• Marca-passo instalado"))
                }
                if (implants.isNotEmpty()) {
                    document.add(Paragraph("• $implants"))
                }
            }
            
            // Contatos de Emergência
            document.add(Paragraph("\nCONTATOS DE EMERGÊNCIA").setBold().setFontSize(14f))
            val contact1Name = json.optString("emergencyContact1Name", "")
            val contact1Phone = json.optString("emergencyContact1Phone", "")
            if (contact1Name.isNotEmpty()) {
                document.add(Paragraph("1. $contact1Name - $contact1Phone"))
            }
            
            val contact2Name = json.optString("emergencyContact2Name", "")
            val contact2Phone = json.optString("emergencyContact2Phone", "")
            if (contact2Name.isNotEmpty()) {
                document.add(Paragraph("2. $contact2Name - $contact2Phone"))
            }
            
            // Observações Adicionais
            val notes = json.optString("additionalNotes", "")
            if (notes.isNotEmpty()) {
                document.add(Paragraph("\nOBSERVAÇÕES ADICIONAIS").setBold().setFontSize(14f))
                document.add(Paragraph(notes))
            }
            
            // Fechar documento
            document.close()
            
            // Agora copiar para Downloads usando MediaStore
            val savedFile = savePDFToDownloads(tempFile, fileName)
            
            if (savedFile != null) {
                Toast.makeText(this, "PDF salvo em Downloads/$fileName", Toast.LENGTH_LONG).show()
                
                // Compartilhar o PDF
                sharePDF(tempFile, fileName)
            } else {
                Toast.makeText(this, "PDF criado em: ${tempFile.absolutePath}", Toast.LENGTH_LONG).show()
                sharePDF(tempFile, fileName)
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao gerar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun savePDFToDownloads(sourceFile: File, fileName: String): File? {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ - usar MediaStore
                val resolver = contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        sourceFile.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    return File(Environment.DIRECTORY_DOWNLOADS, fileName)
                }
            } else {
                // Android 9 e inferior
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val destFile = File(downloadsDir, fileName)
                sourceFile.copyTo(destFile, overwrite = true)
                return destFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
    private fun sharePDF(pdfFile: File, fileName: String) {
        try {
            val uri = androidx.core.content.FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                pdfFile
            )
            
            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                putExtra(android.content.Intent.EXTRA_SUBJECT, "Informações Médicas de Emergência")
                putExtra(android.content.Intent.EXTRA_TEXT, "Documento com informações médicas de emergência")
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            startActivity(android.content.Intent.createChooser(shareIntent, "Compartilhar PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
