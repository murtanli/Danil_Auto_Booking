package com.example.auto_booking.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.auto_booking.MainActivity
import com.example.auto_booking.R
import com.example.auto_booking.about_service_page.Page
import com.example.auto_booking.api.api_resource
import com.example.auto_booking.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as? MainActivity)?.act_bar()

        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "")

        val linear_container = binding.ContainerBlocks

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_all_auto_services()
                if (result.isNotEmpty()) {
                    //вызов функции отрисовки блоков

                    val title = TextView(requireContext())
                    title.text = "Автосервисы"
                    title.setPadding(100,100,100,100)
                    title.gravity = Gravity.CENTER
                    title.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    title.textSize = 30F

                    linear_container.addView(title)

                    for (service in result) {
                        val block = create_block_ser(service.name, service.address, service.pk, service.about)
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

    private fun create_block_ser(name: String, address: String, id: Int, about: String): LinearLayout {
        //общий блок
        val block = LinearLayout(requireContext())

        val blockParams = LinearLayout.LayoutParams(
            1000,
            550
        )
        blockParams.setMargins(0, 0, 10, 0)
        blockParams.bottomMargin = 100
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

        address_text.setPadding(30,30,200,100)
        //address_text.gravity = Gravity.LEFT

        // Создаем параметры макета и устанавливаем их для текстового поля
        val layoutParams_address = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        address_text.layoutParams = layoutParams_address

        block.setOnClickListener{
            val intent = Intent(requireContext(), Page::class.java)
            intent.putExtra("id", id.toString())
            intent.putExtra("about", about)
            intent.putExtra("name", name)
            startActivity(intent)
        }

        block.addView(title)
        block.addView(address_text)

        return block
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}