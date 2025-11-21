package com.emergencia.medica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var welcomeLayout: LinearLayout
    private lateinit var mainMenuLayout: LinearLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        welcomeLayout = findViewById(R.id.welcomeLayout)
        mainMenuLayout = findViewById(R.id.mainMenuLayout)
        
        val btnStartSetup: Button = findViewById(R.id.btnStartSetup)
        val btnEditData: Button = findViewById(R.id.btnEditData)
        val btnViewQRCode: Button = findViewById(R.id.btnViewQRCode)
        val btnScanQRCode: Button = findViewById(R.id.btnScanQRCode)
        val btnEmergencyCall: Button = findViewById(R.id.btnEmergencyCall)
        val btnLockScreen: Button = findViewById(R.id.btnLockScreen)
        val btnCreateShortcut: Button = findViewById(R.id.btnCreateShortcut)
        
        // Verificar se já tem dados salvos
        if (MedicalData.hasData(this)) {
            showMainMenu()
        } else {
            showWelcome()
        }
        
        btnStartSetup.setOnClickListener {
            startActivity(Intent(this, MedicalFormActivity::class.java))
        }
        
        btnEditData.setOnClickListener {
            startActivity(Intent(this, MedicalFormActivity::class.java))
        }
        
        btnViewQRCode.setOnClickListener {
            startActivity(Intent(this, QRCodeActivity::class.java))
        }
        
        btnScanQRCode.setOnClickListener {
            startActivity(Intent(this, ScanQRCodeActivity::class.java))
        }
        
        btnEmergencyCall.setOnClickListener {
            callEmergencyNumber()
        }
        
        btnLockScreen.setOnClickListener {
            startActivity(Intent(this, LockScreenActivity::class.java))
        }
        
        btnCreateShortcut.setOnClickListener {
            startActivity(Intent(this, ShortcutActivity::class.java))
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Atualizar a interface quando voltar à tela principal
        if (MedicalData.hasData(this)) {
            showMainMenu()
        } else {
            showWelcome()
        }
    }
    
    private fun showWelcome() {
        welcomeLayout.visibility = View.VISIBLE
        mainMenuLayout.visibility = View.GONE
    }
    
    private fun showMainMenu() {
        welcomeLayout.visibility = View.GONE
        mainMenuLayout.visibility = View.VISIBLE
        
        // Atualizar informações do usuário
        val medicalData = MedicalData.load(this)
        medicalData?.let {
            val userInfo: TextView = findViewById(R.id.userInfo)
            userInfo.text = "👤 ${it.fullName}\n🩸 ${it.bloodType}"
        }
    }
    
    private fun callEmergencyNumber() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = android.net.Uri.parse("tel:192") // SAMU no Brasil
        startActivity(intent)
    }
}
