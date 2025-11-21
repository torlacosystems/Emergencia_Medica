package com.emergencia.medica

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class EmergencyNotificationService : Service() {

    companion object {
        private const val TAG = "EmergencyNotification"
        private const val CHANNEL_ID = "emergency_medical_channel"
        private const val NOTIFICATION_ID = 1
        
        fun start(context: Context) {
            Log.d(TAG, "Starting notification service")
            val intent = Intent(context, EmergencyNotificationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        
        fun stop(context: Context) {
            Log.d(TAG, "Stopping notification service")
            val intent = Intent(context, EmergencyNotificationService::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate called")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called")
        createNotificationChannel()
        val notification = createNotification()
        try {
            startForeground(NOTIFICATION_ID, notification)
            Log.d(TAG, "Notification started in foreground")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground: ${e.message}")
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Emergência Médica",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Informações médicas de emergência"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(false)
                setSound(null, null)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created")
        }
    }

    private fun createNotification(): Notification {
        val medicalData = MedicalData.load(this)
        
        val title = if (medicalData != null) {
            "🆘 ${medicalData.fullName}"
        } else {
            "🆘 Emergência Médica"
        }
        
        val text = if (medicalData != null) {
            "🩸 ${medicalData.bloodType}" + 
            (if (medicalData.allergies.isNotEmpty()) " | ⚠️ ${medicalData.allergies}" else "")
        } else {
            "Toque para configurar"
        }
        
        Log.d(TAG, "Creating notification: $title - $text")

        // Intent para abrir tela de bloqueio
        val lockScreenIntent = Intent(this, LockScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val lockScreenPendingIntent = PendingIntent.getActivity(
            this,
            0,
            lockScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent para ligar 192
        val emergencyIntent = Intent(Intent.ACTION_DIAL).apply {
            data = android.net.Uri.parse("tel:192")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val emergencyPendingIntent = PendingIntent.getActivity(
            this,
            1,
            emergencyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(lockScreenPendingIntent)
            .addAction(
                android.R.drawable.ic_menu_view,
                "QR Code",
                lockScreenPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_call,
                "192",
                emergencyPendingIntent
            )
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(false)
            .build()
    }
    
    fun updateNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }
}
