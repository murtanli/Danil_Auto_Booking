package com.example.auto_booking.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.auto_booking.R
import com.example.auto_booking.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Sign_in : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val edit_text_login = findViewById<EditText>(R.id.edit_text_login)
        val edit_text_password = findViewById<EditText>(R.id.edit_text_password)
        val edit_text_repeat_pas = findViewById<EditText>(R.id.edit_text_repeat_pas)
        val edit_text_fio = findViewById<EditText>(R.id.edit_text_fio)
        val button_sign_in = findViewById<Button>(R.id.button_login)
        val error_text = findViewById<TextView>(R.id.textView2)


        button_sign_in.setOnClickListener {
            if (!edit_text_login.text.isNullOrEmpty() && !edit_text_password.text.isNullOrEmpty() && !edit_text_repeat_pas.text.isNullOrEmpty()) {

                val loginText = edit_text_login?.text?.toString()
                val passwordText = edit_text_password?.text?.toString()
                val fio_text = edit_text_fio?.text?.toString()
                GlobalScope.launch(Dispatchers.Main) {
                    try {

                        val data = api_resource()
                        val result = data.Sign_in(
                            loginText.toString(),
                            passwordText.toString(),
                            fio_text.toString())

                        if (result != null) {
                            val intent = Intent(this@Sign_in, Login::class.java)
                            startActivity(intent)
                            error_text.text = result.message

                            val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("login", "false")
                            editor.apply()

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            error_text.text = "Ошибка в процессе авторизации ${result.message}"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        error_text.text = "Ошибка входа: Неправильный пароль или профиль уже существует"
                    }
                }
            } else {
                error_text.text = "Пустые поля ! либо пороли не совпадают"
            }
        }

    }
}