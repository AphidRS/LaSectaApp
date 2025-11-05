package com.lasectaapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.lasectaapp.BuildConfig
import com.lasectaapp.R
import com.lasectaapp.URLManager
import kotlinx.coroutines.launch

class FragmentGemini : Fragment(R.layout.fragment_gemini) {

    private val PREF_LAST_QUESTION = "last_gemini_question"
    private val PREF_LAST_ANSWER = "last_gemini_answer"

    // Vistas del layout
    private lateinit var tvLastQuestionContent: TextView
    private lateinit var etNewQuestion: EditText
    private lateinit var btnSendQuestion: Button
    private lateinit var tvLastAnswerContent: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "PREDICCIÓN IA" // Cambia el título de la actividad

        // 1. Enlazamos las vistas del XML con el código
        tvLastQuestionContent = view.findViewById(R.id.tv_last_question_content)
        tvLastAnswerContent = view.findViewById(R.id.tv_last_answer_content)
        etNewQuestion = view.findViewById(R.id.et_new_question)
        btnSendQuestion = view.findViewById(R.id.btn_send_question)

        // 2. Cargamos y mostramos la última pregunta
        loadAndDisplayLastQuestion()

        // 3. Configuramos el listener del botón
        btnSendQuestion.setOnClickListener {
            val userQuestion = etNewQuestion.text.toString()
            if (userQuestion.isNotBlank()) {
                callGemini(userQuestion)
            } else {
                etNewQuestion.error = "La pregunta no puede estar vacía"
            }
        }
    }

    private fun loadAndDisplayLastQuestion() {
        val lastQuestion = getPreference(PREF_LAST_QUESTION)
        val lastAnswer = getPreference(PREF_LAST_ANSWER)

        if (lastQuestion.isNotBlank()) {
            tvLastQuestionContent.text = lastQuestion
            if (lastAnswer.isNotBlank()) {
                tvLastAnswerContent.text = lastAnswer
                tvLastAnswerContent.visibility = View.VISIBLE
            }
        }
    }

    private fun callGemini(userQuestion: String) {
        val calendarioUrl = URLManager.currentCategory?.calendarioUrl ?: "No disponible"

        val prompt = """
            Actúa como un experto en fútbol 7.
            Mi pregunta es: "$userQuestion".
            Para responder, ten en cuenta el contexto de la competición que se encuentra en esta URL del calendario: $calendarioUrl.
            Analiza los posibles resultados de la siguiente jornada basándote en la información que puedas extraer.
            Sé claro y da tu predicción en un formato conciso.
        """.trimIndent()

        // Corrección del nombre del modelo: 1.5 en lugar de 2.5
        val generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.7F
            }
        )

        val loadingDialog = showLoadingDialog()

        lifecycleScope.launch {
            try {
                val response = generativeModel.generateContent(prompt)
                val responseText = response.text ?: "No se pudo obtener una respuesta."
                loadingDialog.dismiss()

                // Guardamos tanto la pregunta como la nueva respuesta
                savePreference(PREF_LAST_QUESTION, userQuestion)
                savePreference(PREF_LAST_ANSWER, responseText)

                // Actualizamos la UI en el hilo principal
                activity?.runOnUiThread {
                    tvLastQuestionContent.text = userQuestion
                    tvLastAnswerContent.text = responseText
                    tvLastAnswerContent.visibility = View.VISIBLE
                    etNewQuestion.text.clear()
                }

                // Opcional: mostrar también un diálogo con la respuesta completa
                showResponseDialog(responseText)

            } catch (e: Exception) {
                loadingDialog.dismiss()
                showResponseDialog("Error al contactar con la IA: ${e.message}")
            }
        }
    }

    private fun savePreference(key: String, value: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getPreference(key: String): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref?.getString(key, "") ?: ""
    }

    private fun showLoadingDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("Analizando datos y generando predicción...")
            .setCancelable(false)
            .show()
    }

    private fun showResponseDialog(responseText: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Predicción de la IA")
            .setMessage(responseText)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
