package com.emergencia.medica

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class EmergencyWidget : AppWidgetProvider() {
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }
    
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    
    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_emergency)
            
            // Verificar se há dados salvos
            val medicalData = MedicalData.load(context)
            
            if (medicalData != null) {
                views.setTextViewText(R.id.widgetUserName, medicalData.fullName)
                views.setTextViewText(R.id.widgetBloodType, "🩸 ${medicalData.bloodType}")
            } else {
                views.setTextViewText(R.id.widgetUserName, "Configure seus dados")
                views.setTextViewText(R.id.widgetBloodType, "")
            }
            
            // Intent para abrir o app
            val openAppIntent = Intent(context, MainActivity::class.java)
            val openAppPendingIntent = PendingIntent.getActivity(
                context,
                0,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetLayout, openAppPendingIntent)
            
            // Intent para ligar para emergência
            val emergencyIntent = Intent(Intent.ACTION_DIAL)
            emergencyIntent.data = android.net.Uri.parse("tel:192")
            val emergencyPendingIntent = PendingIntent.getActivity(
                context,
                1,
                emergencyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetEmergencyButton, emergencyPendingIntent)
            
            // Intent para mostrar QR Code na tela de bloqueio
            val qrIntent = Intent(context, LockScreenActivity::class.java)
            qrIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            val qrPendingIntent = PendingIntent.getActivity(
                context,
                2,
                qrIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetQRButton, qrPendingIntent)
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
