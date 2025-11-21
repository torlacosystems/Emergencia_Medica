package com.emergencia.medica

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class EmergencyAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "EmergencyAccess"
        private var volumeDownPressCount = 0
        private var lastVolumeDownTime = 0L
        private const val PRESS_TIMEOUT = 2000L // 2 segundos entre pressões
        private const val REQUIRED_PRESSES = 3 // 3 cliques no volume down
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Não precisa processar eventos de acessibilidade
    }

    override fun onInterrupt() {
        // Chamado quando o serviço é interrompido
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            val currentTime = System.currentTimeMillis()
            
            // Reset se passou muito tempo desde a última pressão
            if (currentTime - lastVolumeDownTime > PRESS_TIMEOUT) {
                volumeDownPressCount = 0
            }
            
            volumeDownPressCount++
            lastVolumeDownTime = currentTime
            
            Log.d(TAG, "Volume down pressed: $volumeDownPressCount times")
            
            if (volumeDownPressCount >= REQUIRED_PRESSES) {
                volumeDownPressCount = 0
                
                // Verificar se há dados cadastrados
                if (MedicalData.hasData(this)) {
                    Log.d(TAG, "Opening emergency screen")
                    
                    // Abrir tela de emergência
                    val intent = Intent(this, LockScreenActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                               Intent.FLAG_ACTIVITY_CLEAR_TOP or
                               Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                    
                    return true // Consumir o evento
                }
            }
        }
        
        return super.onKeyEvent(event)
    }
}
