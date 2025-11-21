package com.emergencia.medica

import android.content.Context
import org.json.JSONObject

/**
 * Modelo de dados para informações médicas de emergência
 */
data class MedicalData(
    var fullName: String = "",
    var age: Int = 0,
    var bloodType: String = "",
    var preExistingConditions: String = "",
    var currentMedications: String = "",
    var allergies: String = "",
    var hasPacemaker: Boolean = false,
    var hasImplants: String = "",
    var emergencyContact1Name: String = "",
    var emergencyContact1Phone: String = "",
    var emergencyContact2Name: String = "",
    var emergencyContact2Phone: String = "",
    var additionalNotes: String = ""
) {
    
    companion object {
        private const val PREFS_NAME = "MedicalDataPrefs"
        
        fun save(context: Context, data: MedicalData) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            
            editor.putString("fullName", data.fullName)
            editor.putInt("age", data.age)
            editor.putString("bloodType", data.bloodType)
            editor.putString("preExistingConditions", data.preExistingConditions)
            editor.putString("currentMedications", data.currentMedications)
            editor.putString("allergies", data.allergies)
            editor.putBoolean("hasPacemaker", data.hasPacemaker)
            editor.putString("hasImplants", data.hasImplants)
            editor.putString("emergencyContact1Name", data.emergencyContact1Name)
            editor.putString("emergencyContact1Phone", data.emergencyContact1Phone)
            editor.putString("emergencyContact2Name", data.emergencyContact2Name)
            editor.putString("emergencyContact2Phone", data.emergencyContact2Phone)
            editor.putString("additionalNotes", data.additionalNotes)
            editor.putBoolean("hasData", true)
            
            editor.apply()
        }
        
        fun load(context: Context): MedicalData? {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            
            if (!prefs.getBoolean("hasData", false)) {
                return null
            }
            
            return MedicalData(
                fullName = prefs.getString("fullName", "") ?: "",
                age = prefs.getInt("age", 0),
                bloodType = prefs.getString("bloodType", "") ?: "",
                preExistingConditions = prefs.getString("preExistingConditions", "") ?: "",
                currentMedications = prefs.getString("currentMedications", "") ?: "",
                allergies = prefs.getString("allergies", "") ?: "",
                hasPacemaker = prefs.getBoolean("hasPacemaker", false),
                hasImplants = prefs.getString("hasImplants", "") ?: "",
                emergencyContact1Name = prefs.getString("emergencyContact1Name", "") ?: "",
                emergencyContact1Phone = prefs.getString("emergencyContact1Phone", "") ?: "",
                emergencyContact2Name = prefs.getString("emergencyContact2Name", "") ?: "",
                emergencyContact2Phone = prefs.getString("emergencyContact2Phone", "") ?: "",
                additionalNotes = prefs.getString("additionalNotes", "") ?: ""
            )
        }
        
        fun hasData(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getBoolean("hasData", false)
        }
    }
    
    fun toJson(): String {
        val json = JSONObject()
        json.put("fullName", fullName)
        json.put("age", age)
        json.put("bloodType", bloodType)
        json.put("preExistingConditions", preExistingConditions)
        json.put("currentMedications", currentMedications)
        json.put("allergies", allergies)
        json.put("hasPacemaker", hasPacemaker)
        json.put("hasImplants", hasImplants)
        json.put("emergencyContact1Name", emergencyContact1Name)
        json.put("emergencyContact1Phone", emergencyContact1Phone)
        json.put("emergencyContact2Name", emergencyContact2Name)
        json.put("emergencyContact2Phone", emergencyContact2Phone)
        json.put("additionalNotes", additionalNotes)
        return json.toString()
    }
    
    fun toFormattedText(): String {
        return buildString {
            appendLine("🆘 INFORMAÇÕES DE EMERGÊNCIA MÉDICA")
            appendLine("=" .repeat(40))
            appendLine()
            appendLine("👤 DADOS PESSOAIS:")
            appendLine("Nome: $fullName")
            appendLine("Idade: $age anos")
            if (bloodType.isNotEmpty()) appendLine("Tipo Sanguíneo: $bloodType")
            appendLine()
            
            if (preExistingConditions.isNotEmpty()) {
                appendLine("🏥 CONDIÇÕES PRÉ-EXISTENTES:")
                appendLine(preExistingConditions)
                appendLine()
            }
            
            if (currentMedications.isNotEmpty()) {
                appendLine("💊 MEDICAÇÕES EM USO:")
                appendLine(currentMedications)
                appendLine()
            }
            
            if (allergies.isNotEmpty()) {
                appendLine("⚠️ ALERGIAS:")
                appendLine(allergies)
                appendLine()
            }
            
            if (hasPacemaker || hasImplants.isNotEmpty()) {
                appendLine("⚡ DISPOSITIVOS/IMPLANTES:")
                if (hasPacemaker) appendLine("• Marca-passo instalado")
                if (hasImplants.isNotEmpty()) appendLine("• $hasImplants")
                appendLine()
            }
            
            appendLine("📞 CONTATOS DE EMERGÊNCIA:")
            if (emergencyContact1Name.isNotEmpty()) {
                appendLine("1. $emergencyContact1Name - $emergencyContact1Phone")
            }
            if (emergencyContact2Name.isNotEmpty()) {
                appendLine("2. $emergencyContact2Name - $emergencyContact2Phone")
            }
            appendLine()
            
            if (additionalNotes.isNotEmpty()) {
                appendLine("📝 OBSERVAÇÕES ADICIONAIS:")
                appendLine(additionalNotes)
            }
        }
    }
}
