package com.example.auto_booking.about_service_page

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.auto_booking.MainActivity
import com.example.auto_booking.R
import com.example.auto_booking.api.api_resource
import com.example.auto_booking.api.service_list
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Page : AppCompatActivity() {

    private var serviceIdMap = mutableMapOf<Int?, String>()

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        val id = intent.getStringExtra("id")
        val about = intent.getStringExtra("about")
        val name = intent.getStringExtra("name")


        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "")
        val us_id_int = user_id?.toIntOrNull()

        val auto_service_id = id?.toIntOrNull()

        supportActionBar?.hide()

        val title_block = findViewById<TextView>(R.id.Title_page)
        val about_service = findViewById<TextView>(R.id.about_service_block)
        val button_exit = findViewById<Button>(R.id.button_exit)
        val button_sel = findViewById<Button>(R.id.button_sel)
        val block_price = findViewById<LinearLayout>(R.id.block_price)

        title_block.text = name
        about_service.text = about

        button_exit.setOnClickListener {
            val intent = Intent(this@Page, MainActivity::class.java)
            startActivity(intent)
        }

        // Создаем пустой словарь для хранения service_id и имен услуг


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_all_services(auto_service_id)
                if (result.isNotEmpty()) {
                    for (service in result) {
                        val bl = price_block(service.price.toString(), service.name)
                        block_price.addView(bl)
                        // Добавляем запись в словарь, где ключ - service_id, значение - имя услуги
                        serviceIdMap[service.id] = service.name
                    }
                } else {
                    // Обработка случая, когда список пуст
                    Log.e("BusActivity", "Response failed - result is empty")
                }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("BusActivity", "Error during response", e)
                e.printStackTrace()
            }
        }


        button_sel.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Выберите услугу")

            // Создаем пользовательский макет для AlertDialog
            val view = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

            // Находим поля ввода в пользовательском макете
            val spinner = view.findViewById<Spinner>(R.id.service_text)
            val button_date = view.findViewById<Button>(R.id.button_date)
            val button_time = view.findViewById<Button>(R.id.button_time)

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceIdMap.values.toList())
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            spinner.adapter = adapter


            val LayoytParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                300
            )
            spinner.gravity = Gravity.CENTER
            spinner.layoutParams = LayoytParams

            button_date.setOnClickListener {
                val datePickerDialog = DatePickerDialog(this)

                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    // Здесь можно обработать выбранную пользователем дату
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)

                    // Устанавливаем выбранную дату в поле ввода для даты
                    button_date.text = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(selectedDate.time)
                }
                button_time.isEnabled = true
                // Показываем DatePickerDialog
                datePickerDialog.show()
            }

            button_time.setOnClickListener {
                val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_time_picker_dialog, null)

                val hourPicker: NumberPicker = dialogView.findViewById(R.id.hourPicker)
                val minutePicker: NumberPicker = dialogView.findViewById(R.id.minutePicker)

                hourPicker.minValue = 0
                hourPicker.maxValue = 23
                //авто сервис если работает ночью то оставить так а если не работает то убрать не рабочие часы !!!
                val hours = arrayOf("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")
                hourPicker.displayedValues = hours

                minutePicker.minValue = 0
                minutePicker.maxValue = 1
                val minutes = arrayOf("00", "30")
                minutePicker.displayedValues = minutes

                val builder = AlertDialog.Builder(this)
                builder.setView(dialogView)
                builder.setPositiveButton("OK") { dialog, _ ->
                    val selectedHour = hours[hourPicker.value]
                    val selectedMinute = minutes[minutePicker.value]
                    button_time.text = "$selectedHour:$selectedMinute"
                    dialog.dismiss()
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()

            }

            // Устанавливаем пользовательский макет для AlertDialog
            builder.setView(view)

            // Устанавливаем кнопку "OK" и ее обработчик
            builder.setPositiveButton("Записаться") { dialog, which ->


                // Сохраняем введенные данные в переменные
                val selectedService: String = spinner.selectedItem.toString()
                val date: String = button_date.text.toString()
                val time: String = button_time.text.toString()

                val selectedServiceId: Int? = serviceIdMap.entries.firstOrNull { it.value == selectedService }?.key


                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val data = api_resource()
                        val result = data.show_busy_autser(auto_service_id)
                        if (result.isNotEmpty()) {
                            Log.e("666", "$date $time")
                            for (service in result) {
                                if (date == service.date && time == service.time){
                                    val toast = Toast.makeText(this@Page, "Выбранное дата и время уже занято", Toast.LENGTH_LONG)
                                    toast.show()
                                } else {
                                    if (selectedServiceId != null) {
                                        // Если service_id был выбран, используйте его в вашем вызове функции create_req
                                        val response = data.create_req(us_id_int, auto_service_id, selectedServiceId, "$date $time")
                                        Log.e("666", "${response.message} $date $time")
                                        val toast = Toast.makeText(this@Page, "Запись создалась", Toast.LENGTH_LONG)
                                        toast.show()
                                    } else {
                                        // Если service_id не был выбран (например, если список услуг пуст), обработайте эту ситуацию
                                        Log.e("PageActivity", "Selected service_id is null")
                                    }
                                }
                            }


                        } else {
                            // Обработка случая, когда список пуст
                            Log.e("BusActivity", "Response failed - result is empty")

                            //val error = createBusEpty()
                            //BusesContainer.addView(error)
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("BusActivity", "Error during response", e)
                        e.printStackTrace()
                    }
                }
            }
            builder.show()
        }

    }

    private fun price_block(price: String, name: String): LinearLayout {
        val block = LinearLayout(this)

        val blockParams = LinearLayout.LayoutParams(
            950,
            350
        )
        blockParams.setMargins(0, 100, 10, 0)
        blockParams.bottomMargin = 100
        block.layoutParams = blockParams
        block.gravity = Gravity.CENTER
        block.orientation = LinearLayout.HORIZONTAL
        val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_background_page)
        block.background = backgroundDrawable

        val text_name = TextView(this)
        text_name.text = name
        text_name.textSize = 15F
        text_name.gravity = Gravity.LEFT
        text_name.setTextAppearance(R.style.PageTitle)
        text_name.setPadding(10, 0, 0, 0)

// Устанавливаем параметры макета для задания ширины
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, // Ширина
            LinearLayout.LayoutParams.WRAP_CONTENT // Высота
        )
        params.width = 600 // Задайте ваше желаемое значение ширины здесь
        text_name.layoutParams = params

        val text_price = TextView(this)
        text_price.text = price
        text_name.textSize = 17F
        text_price.setTextAppearance(R.style.PageTitle)
        text_price.setPadding(30,0,50,0)

        block.addView(text_name)
        block.addView(text_price)

        return block
    }

    override fun onBackPressed() {

    }
}