package com.example.auto_booking.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.auto_booking.MainActivity
import com.example.auto_booking.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import com.example.auto_booking.api.api_resource


class Login : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val buttonLogin = findViewById<Button>(R.id.button_login)
        val edit_text_login = findViewById<EditText>(R.id.edit_text_login)
        val edit_text_password = findViewById<EditText>(R.id.edit_text_password)
        val text_sign = findViewById<TextView>(R.id.textView3)
        val errorText = findViewById<TextView>(R.id.textView2)

        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val login_save = sharedPreferences.getString("login", "")


        if (login_save == "true"){
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }


        buttonLogin.setOnClickListener {
            val loginText = edit_text_login?.text?.toString()
            val passwordText = edit_text_password?.text?.toString()

            if (loginText.isNullOrBlank() || passwordText.isNullOrBlank()) {
                errorText.text = "Введите данные в поля"
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        // Вызываем функцию logIn для выполнения запроса
                        val data = api_resource()
                        val result = data.login(loginText, passwordText)

                        if (result != null) {
                            if (result.message.toString() != "Неправильный логин, пароль или роль") {
                                // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                                Log.d("LoginActivity", "Login successful")
                                //Log.d("LoginActivity", "User ID: ${result.user_data.user_id}")
                                errorText.text = result.message

                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                try {
                                    editor.putString("user_id", result.user_id.toString())

                                    editor.putString("user_name", result.fio)
                                    Log.e("555", "${result.user_id}  ${result.message}")
                                    editor.putString("login", "true")
                                    editor.apply()
                                } catch(e: Exception) {
                                    editor.putString("user_id", result.user_id.toString())
                                    editor.putString("login", "true")
                                    editor.apply()
                                }

                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                                //ErrorText.setTextColor(R.color.blue)

                            } else {
                                // Если произошла ошибка, выводим сообщение об ошибке
                                Log.e("LoginActivity", "Login failed")
                                errorText.text = result.message
                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("login", "false")
                                editor.apply()
                            }
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            errorText.text = "Ошибка в процессе авторизации ${result.message}"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        errorText.text = "Ошибка входа: Неправильный пароль или профиль не найден"
                        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("login", "false")
                        editor.apply()
                    }
                }
            }

        }


        text_sign.setOnClickListener {
            val intent = Intent(this, Sign_in::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {

    }
}