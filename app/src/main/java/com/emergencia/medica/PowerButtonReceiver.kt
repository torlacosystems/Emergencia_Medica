package com.emergencia.medica

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PowerButtonReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "PowerButtonReceiver"
        private var lastPressTime = 0L
        private const val DOUBLE_PRESS_INTERVAL = 500L // 500ms para duplo clique
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF || intent.action == Intent.ACTION_SCREEN_ON) {
            val currentTime = System.currentTimeMillis()
            
            Log.d(TAG, "Screen event: ${intent.action}")
            
            // Verificar duplo clique
            if (currentTime - lastPressTime < DOUBLE_PRESS_INTERVAL) {
                Log.d(TAG, "Double press detected!")
                
                // Verificar se há dados cadastrados
                if (MedicalData.hasData(context)) {
                    // Abrir tela de emergência
                    val emergencyIntent = Intent(context, LockScreenActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or 
                               Intent.FLAG_ACTIVITY_CLEAR_TOP or
                               Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    context.startActivity(emergencyIntent)
                }
            }
            
            lastPressTime = currentTime
        }
    }
}
