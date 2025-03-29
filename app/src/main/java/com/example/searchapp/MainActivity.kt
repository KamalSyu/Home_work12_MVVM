package com.example.searchapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: Button
    private lateinit var textViewResult: TextView
    private lateinit var progressBar: ProgressBar

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch = findViewById(R.id.buttonSearch)
        textViewResult = findViewById(R.id.textViewResult)
        progressBar = findViewById(R.id.progressBar)

        // Изначально кнопка "Поиск" должна быть заблокирована
        buttonSearch.isEnabled = false

        // Наблюдение за результатом поиска
        viewModel.resultText.observe(this) { result ->
            textViewResult.text = result
        }

        // Наблюдение за состоянием загрузки
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            buttonSearch.isEnabled = !isLoading && editTextSearch.text.length >= 3
        }

        // Установка TextWatcher для EditText
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Проверяем, что isLoading не равен null
                val isLoading = viewModel.isLoading.value ?: false
                buttonSearch.isEnabled = s?.length ?: 0 >= 3 && !isLoading
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Установка обработчика на кнопку поиска
        buttonSearch.setOnClickListener {
            val query = editTextSearch.text.toString()
            viewModel.search(query)
        }
    }
}