package com.emergencia.medica

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MedicalFormActivity : AppCompatActivity() {
    
    private lateinit var etFullName: EditText
    private lateinit var etAge: EditText
    private lateinit var spinnerBloodType: Spinner
    private lateinit var etPreExistingConditions: EditText
    private lateinit var etCurrentMedications: EditText
    private lateinit var etAllergies: EditText
    private lateinit var checkPacemaker: CheckBox
    private lateinit var etImplants: EditText
    private lateinit var etContact1Name: EditText
    private lateinit var etContact1Phone: EditText
    private lateinit var etContact2Name: EditText
    private lateinit var etContact2Phone: EditText
    private lateinit var etAdditionalNotes: EditText
    private lateinit var btnSave: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_form)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        initViews()
        setupBloodTypeSpinner()
        loadExistingData()
        
        btnSave.setOnClickListener {
            saveData()
        }
    }
    
    private fun initViews() {
        etFullName = findViewById(R.id.etFullName)
        etAge = findViewById(R.id.etAge)
        spinnerBloodType = findViewById(R.id.spinnerBloodType)
        etPreExistingConditions = findViewById(R.id.etPreExistingConditions)
        etCurrentMedications = findViewById(R.id.etCurrentMedications)
        etAllergies = findViewById(R.id.etAllergies)
        checkPacemaker = findViewById(R.id.checkPacemaker)
        etImplants = findViewById(R.id.etImplants)
        etContact1Name = findViewById(R.id.etContact1Name)
        etContact1Phone = findViewById(R.id.etContact1Phone)
        etContact2Name = findViewById(R.id.etContact2Name)
        etContact2Phone = findViewById(R.id.etContact2Phone)
        etAdditionalNotes = findViewById(R.id.etAdditionalNotes)
        btnSave = findViewById(R.id.btnSave)
    }
    
    private fun setupBloodTypeSpinner() {
        val bloodTypes = arrayOf("Selecione", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodType.adapter = adapter
    }
    
    private fun loadExistingData() {
        val data = MedicalData.load(this) ?: return
        
        etFullName.setText(data.fullName)
        etAge.setText(if (data.age > 0) data.age.toString() else "")
        
        // Selecionar tipo sanguíneo
        val bloodTypes = arrayOf("Selecione", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val position = bloodTypes.indexOf(data.bloodType)
        if (position >= 0) {
            spinnerBloodType.setSelection(position)
        }
        
        etPreExistingConditions.setText(data.preExistingConditions)
        etCurrentMedications.setText(data.currentMedications)
        etAllergies.setText(data.allergies)
        checkPacemaker.isChecked = data.hasPacemaker
        etImplants.setText(data.hasImplants)
        etContact1Name.setText(data.emergencyContact1Name)
        etContact1Phone.setText(data.emergencyContact1Phone)
        etContact2Name.setText(data.emergencyContact2Name)
        etContact2Phone.setText(data.emergencyContact2Phone)
        etAdditionalNotes.setText(data.additionalNotes)
    }
    
    private fun saveData() {
        val fullName = etFullName.text.toString().trim()
        val ageStr = etAge.text.toString().trim()
        
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Por favor, insira seu nome completo", Toast.LENGTH_SHORT).show()
            etFullName.requestFocus()
            return
        }
        
        if (ageStr.isEmpty()) {
            Toast.makeText(this, "Por favor, insira sua idade", Toast.LENGTH_SHORT).show()
            etAge.requestFocus()
            return
        }
        
        val age = ageStr.toIntOrNull() ?: 0
        if (age <= 0 || age > 150) {
            Toast.makeText(this, "Por favor, insira uma idade válida", Toast.LENGTH_SHORT).show()
            etAge.requestFocus()
            return
        }
        
        val bloodType = spinnerBloodType.selectedItem.toString()
        if (bloodType == "Selecione") {
            Toast.makeText(this, "Por favor, selecione seu tipo sanguíneo", Toast.LENGTH_SHORT).show()
            return
        }
        
        val medicalData = MedicalData(
            fullName = fullName,
            age = age,
            bloodType = bloodType,
            preExistingConditions = etPreExistingConditions.text.toString().trim(),
            currentMedications = etCurrentMedications.text.toString().trim(),
            allergies = etAllergies.text.toString().trim(),
            hasPacemaker = checkPacemaker.isChecked,
            hasImplants = etImplants.text.toString().trim(),
            emergencyContact1Name = etContact1Name.text.toString().trim(),
            emergencyContact1Phone = etContact1Phone.text.toString().trim(),
            emergencyContact2Name = etContact2Name.text.toString().trim(),
            emergencyContact2Phone = etContact2Phone.text.toString().trim(),
            additionalNotes = etAdditionalNotes.text.toString().trim()
        )
        
        MedicalData.save(this, medicalData)
        
        // Iniciar notificação persistente automaticamente
        val prefs = getSharedPreferences("emergency_prefs", android.content.Context.MODE_PRIVATE)
        val notificationEnabled = prefs.getBoolean("notification_enabled", false)
        if (notificationEnabled) {
            EmergencyNotificationService.start(this)
        }
        
        Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
