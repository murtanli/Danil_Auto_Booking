package com.example.auto_booking.api



data class loginResponse(
    val message: String,
    val user_id: Int,
    val fio: String
)

data class sign_inResponse(
    val message: String
)


data class services(
    val name: String,
    val price: Float
)

data class auto_services (
    val pk: Int,
    val name: String,
    val about: String,
    val address: String,
    val services: List<services>
)

data class service_list(
    val id: Int,
    val name: String,
    val price: Float
)
data class busy_ser(
    val services: String,
    val date: String,
    val time: String
)

data class response(
    val rec: busy_ser
)

data class sel_res(
    val auto_service_name: String,
    val auto_service_address: String,
    val service: String,
    val date: String,
    val time: String
)