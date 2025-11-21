package com.emergencia.medica

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.net.URLEncoder

class LockScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configurar para mostrar sobre a tela de bloqueio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        setContentView(R.layout.activity_lock_screen)

        val medicalData = MedicalData.load(this)
        
        if (medicalData == null || medicalData.fullName.isEmpty()) {
            Toast.makeText(this, "Nenhum dado médico cadastrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Exibir informações básicas
        findViewById<TextView>(R.id.tvLockScreenName).text = medicalData.fullName
        findViewById<TextView>(R.id.tvLockScreenBloodType).text = "Tipo Sanguíneo: ${medicalData.bloodType}"
        findViewById<TextView>(R.id.tvLockScreenAllergies).text = 
            if (medicalData.allergies.isNotEmpty()) "⚠️ Alergias: ${medicalData.allergies}" else ""

        // Gerar QR Code
        val qrCodeImage = findViewById<ImageView>(R.id.ivLockScreenQRCode)
        val qrBitmap = generateQRCode(medicalData)
        qrCodeImage.setImageBitmap(qrBitmap)

        // Botão de emergência
        findViewById<TextView>(R.id.btnLockScreenEmergency).setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                data = android.net.Uri.parse("tel:192")
            }
            startActivity(intent)
        }

        // Botão fechar
        findViewById<TextView>(R.id.btnLockScreenClose).setOnClickListener {
            finish()
        }
    }

    private fun generateQRCode(data: MedicalData): Bitmap {
        val url = generateWebURL(data)
        val size = 512
        
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)
        
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }
        
        return bitmap
    }

    private fun generateWebURL(data: MedicalData): String {
        val json = data.toJson()
        val encodedData = URLEncoder.encode(json, "UTF-8")
        return "https://torlacosystems.github.io/Emergencia_Medica/?data=$encodedData"
    }
}
