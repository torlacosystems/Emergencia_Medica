package com.emergencia.medica

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ShortcutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shortcut)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Acesso Rápido"

        findViewById<Button>(R.id.btnEnableAccessibility).setOnClickListener {
            enableAccessibilityService()
        }

        findViewById<Button>(R.id.btnCreateShortcut).setOnClickListener {
            createShortcut()
        }
    }

    private fun enableAccessibilityService() {
        if (!MedicalData.hasData(this)) {
            Toast.makeText(this, "Configure seus dados médicos primeiro", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Ativar Botão Volume")
            .setMessage("Para ativar o acesso por botão Volume:\n\n" +
                    "1. Vá em Configurações > Acessibilidade\n" +
                    "2. Procure por 'Emergência Médica'\n" +
                    "3. Ative o serviço\n\n" +
                    "Depois, pressione VOLUME DOWN 3 vezes para abrir o QR Code, mesmo com celular bloqueado.")
            .setPositiveButton("Ir para Configurações") { _, _ ->
                try {
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                } catch (e: Exception) {
                    Toast.makeText(this, "Erro ao abrir configurações", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun createShortcut() {
        if (!MedicalData.hasData(this)) {
            Toast.makeText(this, "Configure seus dados médicos primeiro", Toast.LENGTH_SHORT).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager = getSystemService(ShortcutManager::class.java)
            
            if (shortcutManager?.isRequestPinShortcutSupported == true) {
                val intent = Intent(this, LockScreenActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                val shortcut = ShortcutInfo.Builder(this, "emergency_qr")
                    .setShortLabel("SOS QR")
                    .setLongLabel("Emergência Médica QR Code")
                    .setIcon(Icon.createWithResource(this, android.R.drawable.ic_dialog_alert))
                    .setIntent(intent)
                    .build()

                shortcutManager.requestPinShortcut(shortcut, null)
                Toast.makeText(this, "Atalho criado! Adicione à tela inicial", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Seu dispositivo não suporta atalhos", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Para Android 7.1 e anteriores
            val shortcutIntent = Intent(this, LockScreenActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            }

            val addIntent = Intent("com.android.launcher.action.INSTALL_SHORTCUT").apply {
                putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
                putExtra(Intent.EXTRA_SHORTCUT_NAME, "SOS QR")
                putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, 
                    Intent.ShortcutIconResource.fromContext(this@ShortcutActivity, 
                        android.R.drawable.ic_dialog_alert))
            }

            sendBroadcast(addIntent)
            Toast.makeText(this, "Atalho criado na tela inicial!", Toast.LENGTH_LONG).show()
        }

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
