package com.example.auto_booking.api

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class api_resource {


    suspend fun login(login: String, password: String): loginResponse {
        val apiUrl = "http://194.58.121.142:8100/login/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"login\":\"$login\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), loginResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }

    suspend fun Sign_in(login:String, password: String, fio: String): sign_inResponse {
        val apiUrl = "http://194.58.121.142:8100/sign_in/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"login\":\"$login\",\"fio\":\"$fio\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), sign_inResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing register data ", e)
                throw e
            }
        }
    }

    suspend fun get_all_auto_services(): List<auto_services> {
        val apiUrl = "http://194.58.121.142:8100/get_all_autos_ser/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val driversArray = gson.fromJson(response.toString(), Array<auto_services>::class.java)
                driversArray.toList()
            } catch (e: Exception) {
                // Обработка ошибок, например, логирование
                throw e
            }
        }
    }

    suspend fun get_all_services(auto_service_id: Int?): List<service_list> {
        val apiUrl = "http://194.58.121.142:8100/get_all_service/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"auto_service_id\":\"$auto_service_id\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val driversArray = gson.fromJson(response.toString(), Array<service_list>::class.java)
                driversArray.toList()
            } catch (e: Exception) {
                // Обработка ошибок, например, логирование
                throw e
            }
        }
    }

    suspend fun create_req(user_id: Int?, auto_service_id: Int?, service_id: Int?, date_time: String): sign_inResponse {
        val apiUrl = "http://194.58.121.142:8100/create_req/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"user_id\":\"$user_id\",\"auto_service_id\":\"$auto_service_id\",\"service_id\":\"$service_id\",\"date_time\":\"$date_time\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), sign_inResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing register data ", e)
                throw e
            }
        }
    }

    suspend fun show_busy_autser(auto_service_id: Int?): List<busy_ser> {
        val apiUrl = "http://194.58.121.142:8100/show_busy_req/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                val jsonInputString = "{\"auto_service_id\":$auto_service_id}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                // Обрабатываем полученный JSON-ответ
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val cargoArray = gson.fromJson(response.toString(), Array<busy_ser>::class.java)
                cargoArray.toList()
            } catch (e: Exception) {
                // Обработка ошибок, например, логирование
                throw e
            }
        }
    }

    suspend fun show_busy_autser_user(user_id: Int?): List<sel_res> {
        val apiUrl = "http://194.58.121.142:8100/show_busy_req_user/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                val jsonInputString = "{\"user_id\":$user_id}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                // Обрабатываем полученный JSON-ответ
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val cargoArray = gson.fromJson(response.toString(), Array<sel_res>::class.java)
                cargoArray.toList()
            } catch (e: Exception) {
                // Обработка ошибок, например, логирование
                throw e
            }
        }
    }
}