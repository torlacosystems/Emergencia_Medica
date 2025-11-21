package com.emergencia.medica

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.net.Uri
import java.net.URLEncoder

class QRCodeActivity : AppCompatActivity() {
    
    private lateinit var ivQRCode: ImageView
    private lateinit var tvMedicalInfo: TextView
    private lateinit var btnShare: Button
    private lateinit var btnSave: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "QR Code de Emergência"
        
        ivQRCode = findViewById(R.id.ivQRCode)
        tvMedicalInfo = findViewById(R.id.tvMedicalInfo)
        btnShare = findViewById(R.id.btnShare)
        btnSave = findViewById(R.id.btnSave)
        
        val medicalData = MedicalData.load(this)
        if (medicalData == null) {
            Toast.makeText(this, "Nenhum dado médico encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        // Exibir informações formatadas
        tvMedicalInfo.text = medicalData.toFormattedText()
        
        // Gerar URL com dados codificados para página web
        val webUrl = generateWebURL(medicalData.toJson())
        
        // Gerar QR Code com o URL
        generateQRCode(webUrl)
        
        btnShare.setOnClickListener {
            shareQRCode()
        }
        
        btnSave.setOnClickListener {
            saveQRCodeToGallery()
        }
    }
    
    private fun generateWebURL(jsonData: String): String {
        // URL da página hospedada no GitHub Pages
        val baseUrl = "https://torlacosystems.github.io/Emergencia_Medica"
        val encodedData = URLEncoder.encode(jsonData, "UTF-8")
        return "$baseUrl?data=$encodedData"
    }
    
    private fun generateQRCode(content: String) {
        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            
            ivQRCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao gerar QR Code: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun shareQRCode() {
        try {
            val bitmap = (ivQRCode.drawable as BitmapDrawable).bitmap
            val path = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "QR_Code_Emergencia_${System.currentTimeMillis()}",
                "QR Code com informações médicas de emergência"
            )
            
            val uri = android.net.Uri.parse(path)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Meu QR Code de emergência médica")
            startActivity(Intent.createChooser(shareIntent, "Compartilhar QR Code"))
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao compartilhar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun saveQRCodeToGallery() {
        try {
            val bitmap = (ivQRCode.drawable as BitmapDrawable).bitmap
            MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "QR_Code_Emergencia_${System.currentTimeMillis()}",
                "QR Code com informações médicas de emergência"
            )
            Toast.makeText(this, "QR Code salvo na galeria!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
