package com.example.auto_booking.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.auto_booking.R
import com.example.auto_booking.about_service_page.Page
import com.example.auto_booking.api.api_resource
import com.example.auto_booking.auth.Login
import com.example.auto_booking.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fio_block = binding.textFioBlockProfile
        val linear_container = binding.containerProfile
        val button_exit = binding.buttonExit

        button_exit.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("user_id")
            editor.remove("user_name")
            editor.putString("login", "false")
            editor.apply()
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val user_name = sharedPreferences.getString("user_name", "")
        val user_id = sharedPreferences.getString("user_id", "")

        val id = user_id?.toIntOrNull()

        fio_block.text = "Логин - ${user_name}"


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.show_busy_autser_user(id)
                if (result.isNotEmpty()) {
                    //вызов функции отрисовки блоков

                    for (service in result) {
                        val block = create_block_ser(service.auto_service_name, service.auto_service_address, service.service, service.date, service.time)
                        linear_container.addView(block)
                        linear_container.gravity = Gravity.CENTER
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


        return root
    }

    private fun create_block_ser(name: String, address: String, service: String, date: String, time: String): LinearLayout {
        //общий блок
        val block = LinearLayout(requireContext())

        val blockParams = LinearLayout.LayoutParams(
            1100,
            750
        )
        blockParams.setMargins(0, 0, 10, 0)
        blockParams.bottomMargin = 200
        block.layoutParams = blockParams
        block.gravity = Gravity.CENTER
        block.orientation = LinearLayout.VERTICAL
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background)
        block.background = backgroundDrawable

        //name
        val title = TextView(requireContext())
        title.text = name
        title.setTextAppearance(R.style.PageTitle)

        title.setPadding(30,50,30,0)
        title.gravity = Gravity.LEFT

        // Создаем параметры макета и устанавливаем их для текстового поля
        val layoutParams_title = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        title.layoutParams = layoutParams_title

        //address
        val address_text = TextView(requireContext())
        address_text.text = "Адрес - ${address}"
        address_text.setTextAppearance(R.style.TextInf)

        address_text.setPadding(30,30,200,0)
        //address_text.gravity = Gravity.LEFT

        // Создаем параметры макета и устанавливаем их для текстового поля
        val layoutParams_address = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        address_text.layoutParams = layoutParams_address

        //название услуги
        val service_text = TextView(requireContext())
        service_text.text = "Работа - ${service}"
        service_text.setTextAppearance(R.style.TextInf)

        service_text.setPadding(30,30,200,0)
        //address_text.gravity = Gravity.LEFT

        // Создаем параметры макета и устанавливаем их для текстового поля
        val layoutParams_service = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        service_text.layoutParams = layoutParams_service

        //Дата и время записи
        val datetime_text = TextView(requireContext())
        datetime_text.text = "Дата и время записи - ${date} $time"
        datetime_text.setTextAppearance(R.style.TextInf)

        datetime_text.setPadding(30,30,200,100)
        //address_text.gravity = Gravity.LEFT

        // Создаем параметры макета и устанавливаем их для текстового поля
        val layoutParams_datetime = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        datetime_text.layoutParams = layoutParams_datetime

        block.addView(title)
        block.addView(address_text)
        block.addView(service_text)
        block.addView(datetime_text)

        return block
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}